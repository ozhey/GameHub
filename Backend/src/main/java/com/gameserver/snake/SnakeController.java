package com.gameserver.snake;

import java.util.ArrayList;
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

import com.gameserver.common.models.UserSession;
import com.gameserver.snake.models.Game;
import com.gameserver.snake.models.WebsocketCommand;

@Controller
public class SnakeController {

  private Map<String, Game> roomIdToGame = new HashMap<>();
  private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();
  private Map<String, ArrayList<String>> playersByRoom = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate smp;

  @MessageMapping("/snake_room/{roomId}")
  public void command(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId, WebsocketCommand message)
      throws Exception {
    Game game = roomIdToGame.get(roomId);
    ArrayList<String> players = playersByRoom.getOrDefault(roomId, new ArrayList<String>());
    switch (message.getType()) {
      case "startGame":
        if (game == null) {
          log("A new game has been created for room " + roomId);
          game = new Game(smp, players, roomId);
          roomIdToGame.put(roomId, game);
        }
        game.resetGame(players);
        Map<String, String> playerIdToColor = game.FetchSnakePlayerIdToColor();
        smp.convertAndSend("/topic/snake_room/" + roomId, playerIdToColor);
        game.start(players);
        smp.convertAndSend("/topic/snake_room/" + roomId, game);
        break;
      case "changeDir":
        String playerId = sessionIdToUserSession.get(sessionId).getPlayerId();
        game.changeSnakeDir(playerId, message.getContent());
        break;
      case "registerPlayer":
        String username = message.getContent();
        sessionIdToUserSession.put(sessionId, new UserSession(roomId, username));
        players.add(username);
        playersByRoom.put(roomId, players);
        log("room " + roomId + " has " + players.size() + " players");
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
  }

  @EventListener
  public void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
    handleLeaveRoom(event);
  }

  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    handleLeaveRoom(event);
  }

  // update gameStateMap and subIdToRoomId every time a user unsubscribes or
  // disconnects from a room
  private void handleLeaveRoom(AbstractSubProtocolEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessId = headerAccessor.getSessionId();
    if (!sessionIdToUserSession.containsKey(sessId)) { // user does not play snake
      return;
    }
    UserSession userSession = sessionIdToUserSession.get(sessId);
    String roomId = userSession.getRoomId();
    sessionIdToUserSession.remove(sessId);

    ArrayList<String> players = playersByRoom.getOrDefault(roomId, new ArrayList<String>());
    players.remove(userSession.getPlayerId());
    playersByRoom.put(roomId, players);

    log("room " + roomId + " has " + players.size() + " players");

    // if a game is in progress and there are no players, kill room
    if (players.size() <= 0 && roomIdToGame.containsKey(roomId)) {
      roomIdToGame.get(roomId).resetTimer();
      roomIdToGame.remove(roomId);
      log("room " + roomId + " is empty, deleting game instance");
    }

  }

  private void log(String log) {
    System.out.println("[snake]: " + log);
  }

}