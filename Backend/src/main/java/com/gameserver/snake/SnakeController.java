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
import com.gameserver.snake.models.Session;

@Controller
public class SnakeController {

  private Map<String, Game> roomIdToGame = new HashMap<>();
  private Map<String, Session> sessionIdToSession = new HashMap<>();
  private Map<String, Integer> roomIdToPlayersCount = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/snake_room/{roomId}")
  public void command(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId, String message)
      throws Exception {
    Game game = roomIdToGame.get(roomId);
    switch (message) {
      case "start":
        int playersCount = roomIdToPlayersCount.getOrDefault(roomId, 0);
        if (game == null) {
          game = new Game(messagingTemplate, playersCount, roomId);
          roomIdToGame.put(roomId, game);
        }
        game.start(playersCount);
        messagingTemplate.convertAndSend("/topic/snake_room/" + roomId, game);
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
    if (headerAccessor.getDestination() != null &&
        !headerAccessor.getDestination().startsWith("/topic/snake")) { // player subscribed to other game
      return;
    }
    String sessId = headerAccessor.getSessionId();
    String roomId = extractRoomIdFromDestination(headerAccessor.getDestination());
    int playerId = roomIdToPlayersCount.getOrDefault(roomId, 0);
    sessionIdToSession.put(sessId, new Session(roomId, playerId));
    roomIdToPlayersCount.put(roomId, ++playerId);
    System.out.println("room " + roomId + " has " + playerId + " players");
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

    int numOfPlayers = roomIdToPlayersCount.getOrDefault(roomId, 0);
    roomIdToPlayersCount.put(roomId, --numOfPlayers);

    // if a game is in progress and there are no players, kill room
    if (numOfPlayers <= 0 && roomIdToGame.containsKey(roomId)) {
      roomIdToGame.get(roomId).resetTimer();
      roomIdToGame.remove(roomId);
    }

    System.out.println("room " + roomId + " has " + numOfPlayers + " players");
  }

}