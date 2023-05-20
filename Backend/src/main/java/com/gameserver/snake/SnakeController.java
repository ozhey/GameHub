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

import com.gameserver.gameutils.models.UserSession;
import com.gameserver.snake.models.Game;
import com.gameserver.snake.models.WebsocketCommand;
import com.gameserver.snake.persistence.SnakeScoreService;

@Controller
public class SnakeController {

  private Map<String, Game> roomIdToGame = new HashMap<>();
  private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();
  private Map<String, ArrayList<String>> playersByRoom = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate smp;

  @Autowired
  private SnakeScoreService snakeScoreService;

  /**
   * This method handles the commands that are sent by the clients.
   * 
   * @param roomId    The ID of the room.
   * @param sessionId The ID of the session.
   * @param message   The command message.
   * @throws Exception If an error occurs.
   */
  @MessageMapping("/snake_room/{roomId}")
  public void command(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId,
      WebsocketCommand message)
      throws Exception {
    Game game = roomIdToGame.get(roomId);
    ArrayList<String> players = playersByRoom.getOrDefault(roomId, new ArrayList<String>());
    switch (message.getType()) {
      case "startGame":
        if (game == null) {
          log("A new game has been created for room " + roomId);
          game = new Game(smp, players, roomId, snakeScoreService);
          roomIdToGame.put(roomId, game);
        }
        game.resetGame(players);
        Map<String, String> playerIdToColor = game.fetchSnakePlayerIdToColor();
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
        // prevent players from playing with themselves
        if (!players.contains(username)) {
          players.add(username);
        }
        playersByRoom.put(roomId, players);
        log("room " + roomId + " has " + players.size() + " players");
        break;
    }
  }

  /**
   * This method is called when a client unsubscribes from a room.
   * 
   * @param event The event object.
   */
  @EventListener
  public void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
    handleLeaveRoom(event);
  }

  /**
   * This method is called when a client disconnects from a room.
   * 
   * @param event The event object.
   */
  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    handleLeaveRoom(event);
  }

  /**
   * This method is called when a client leaves a room.
   * It updates the gameStateMap and subIdToRoomId every time a user unsubscribes
   * or disconnects from a room
   * 
   * @param event The event object.
   */
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

  
  /** 
   * A wrapper for logging snake game logs
   * @param log the log text
   */
  private void log(String log) {
    System.out.println("[snake]: " + log);
  }

}