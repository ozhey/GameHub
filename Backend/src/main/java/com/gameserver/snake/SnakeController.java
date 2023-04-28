package com.gameserver.snake;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.gameserver.snake.models.Game;
import com.gameserver.snake.models.UserSession;

@Controller
public class SnakeController {

  private Map<String, Game> roomIdToGame = new HashMap<>();
  private Map<String, UserSession> sessionIdToSession = new HashMap<>();
  private Map<String, Integer> playersNumByRoom = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate smp;

  @MessageMapping("/snake_room/{roomId}")
  public void command(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId, String message)
      throws Exception {
    Game game = roomIdToGame.get(roomId);
    switch (message) {
      case "start":
        int playersCount = playersNumByRoom.getOrDefault(roomId, 0);
        if (game == null) {
          log("A new game has been created for room " + roomId);
          game = new Game(smp, playersCount, roomId);
          roomIdToGame.put(roomId, game);
        }
        game.start(playersCount);
        smp.convertAndSend("/topic/snake_room/" + roomId, game);
        break;
      case "ArrowUp":
      case "ArrowDown":
      case "ArrowLeft":
      case "ArrowRight":
        int playerId = sessionIdToSession.get(sessionId).getPlayerId();
        game.changeSnakeDir(playerId, message);
        break;
      default:
        break;
    }
  }

  @EventListener
  public void handleSessionSubscribe(SessionSubscribeEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String dest = headerAccessor.getDestination();
    if (dest != null && !dest.startsWith("/topic/snake")) { // player subscribed to other game
      return;
    }
    String sessId = headerAccessor.getSessionId();
    String roomId = extractRoomIdFromDestination(headerAccessor.getDestination());
    int playerId = playersNumByRoom.getOrDefault(roomId, 0);
    sessionIdToSession.put(sessId, new UserSession(roomId, playerId));
    playersNumByRoom.put(roomId, ++playerId);
    log("room " + roomId + " has " + playerId + " players");
  }

  @EventListener
  public void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
    handleLeaveRoom(event);
  }

  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    handleLeaveRoom(event);
  }

  private String extractRoomIdFromDestination(String destination) {
    // Assuming the destination format is "/room/{roomId}"
    int lastSlashIndex = destination.lastIndexOf("/");
    return destination.substring(lastSlashIndex + 1);
  }

  // update gameStateMap and subIdToRoomId every time a user unsubscribes or
  // disconnects from a room
  private void handleLeaveRoom(AbstractSubProtocolEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessId = headerAccessor.getSessionId();
    if (!sessionIdToSession.containsKey(sessId)) { // user does not play snake
      return;
    }
    String roomId = sessionIdToSession.get(sessId).getRoomId();
    sessionIdToSession.remove(sessId);

    int numOfPlayers = playersNumByRoom.getOrDefault(roomId, 0);
    playersNumByRoom.put(roomId, --numOfPlayers);

    log("room " + roomId + " has " + numOfPlayers + " players");

    // if a game is in progress and there are no players, kill room
    if (numOfPlayers <= 0 && roomIdToGame.containsKey(roomId)) {
      roomIdToGame.get(roomId).resetTimer();
      roomIdToGame.remove(roomId);
      log("room " + roomId + " is empty, deleting game instance");
    }

  }

  private void log(String log) {
    System.out.println("[snake]: " + log);
  }

}