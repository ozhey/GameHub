package com.gameserver.tictactoe;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.gameserver.tictactoe.models.Game;
import com.gameserver.tictactoe.models.WebsocketCommand;
import com.gameserver.tictactoe.persistence.TTTScoreService;

/**
 * Controller class for TicTacToe game.
 */
@Controller
public class TicTacToeController {

  private static final int ROOM_ID_LENGTH = 8;

  private String waitingUserSessId = "";
  private Map<String, Game> roomIdToGame = new HashMap<>();
  private Map<String, UserSession> sessIdToUserSession = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate smp;

  @Autowired
  private TTTScoreService tttScoreService;

  /**
   * Handles WebSocket commands for TicTacToe game.
   * 
   * @param roomId    The ID of the game room.
   * @param sessionId The WebSocket session ID.
   * @param message   The WebSocket command message. The available command types
   *                  are "registerPlayer", "startGame", "makeMove".
   * @throws Exception if an error occurs during command processing.
   */
  @MessageMapping("/ttt_room/{roomId}")
  public synchronized void command(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId,
      WebsocketCommand message) throws Exception {
    Game game = roomIdToGame.get(roomId);
    if (game == null) {
      game = new Game(smp, roomId, tttScoreService);
      this.roomIdToGame.put(roomId, game);
      log("A new game has been created for room " + roomId);
    }

    switch (message.getCommand()) {
      case "registerPlayer":
        handleRegisterPlayer(sessionId, roomId, message.getContent(), game);
        break;
      case "startGame":
        game.resetGame();
        break;
      case "makeMove":
        game = roomIdToGame.get(roomId);
        if (game != null) {
          UserSession userSession = this.sessIdToUserSession.get(sessionId);
          game.makeMove(message.getRow(), message.getCol(), userSession.getPlayerId());
        }
        break;
    }
  }

  /**
   * Handles the session subscribe event.
   * 
   * @param event The session subscribe event.
   */
  @EventListener
  public synchronized void handleSessionSubscribe(SessionSubscribeEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String dest = headerAccessor.getDestination();
    if (dest == null) {
      return;
    }

    // subscribed to waiting room
    if (dest.equals("/topic/ttt/waiting_room")) {
      handleJoinWaitingRoom(headerAccessor);
      String sessId = headerAccessor.getSessionId();
      // we add a dummy value just to be able to know that the player is currently
      // playing tic-tac-toe
      this.sessIdToUserSession.put(sessId, new UserSession("waitingRoom", ""));
      return;
    }

  }

  /**
   * Handles the session unsubscribe event.
   * 
   * @param event The session unsubscribe event.
   */
  @EventListener
  public void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
    handleLeaveGame(event);
  }

  /**
   * Handles the session disconnect event.
   * 
   * @param event The session disconnect event.
   */
  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    handleLeaveGame(event);
  }

  /**
   * Handles the event when a user leaves the game.
   * If the user was in a waiting room, it will empty the waiting room.
   * If the user left an active game, it will delete the game and notify the other
   * player (if one exists).
   *
   * @param event The AbstractSubProtocolEvent representing the event of the user
   *              leaving the game.
   */
  private synchronized void handleLeaveGame(AbstractSubProtocolEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessId = headerAccessor.getSessionId();

    UserSession userSession = this.sessIdToUserSession.get(sessId);
    // if user did not play tic tac toe, return
    if (userSession == null) {
      return;
    }

    String roomId = userSession.getRoomId();
    if (roomId == "waitingRoom") {
      this.waitingUserSessId = "";
      log("user " + sessId + " left the waiting room");
      return;
    }

    if (roomId != null && this.roomIdToGame.containsKey(roomId)) { // player left an active game
      log("user " + sessId + " left room " + roomId + ". letting the other player know and deleting game instance");
      this.smp.convertAndSend("/topic/ttt_room/" + roomId, "end");
      this.roomIdToGame.remove(roomId);
      this.sessIdToUserSession.remove(roomId);
      return;
    }

    if (roomId != null) { // player left a game but he was already alone
      log("user " + sessId + " left room " + roomId);
      this.sessIdToUserSession.remove(roomId);
    }

  }

  /**
   * Generates a unique room ID.
   *
   * @return A String representing the generated room ID.
   */
  private String generateRoomId() {
    return UUID.randomUUID().toString().substring(0, ROOM_ID_LENGTH);
  }

  /**
   * Handles the event when a user joins the waiting room.
   * If someone already waits in the waiting room, it it creates a new room UUID
   * and sends it to both clients.
   *
   * @param headerAccessor The StompHeaderAccessor containing the header
   *                       information of the event.
   */
  private void handleJoinWaitingRoom(StompHeaderAccessor headerAccessor) {
    String sessId = headerAccessor.getSessionId();
    if (this.waitingUserSessId.equals("")) {
      log("user " + sessId + " is waiting for a match");
      this.waitingUserSessId = sessId;
      return;
    }

    // a short wait to let the new client establish the connection, otherwise the
    // message for some reason sometimes drops
    try {
      wait(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String roomId = generateRoomId(); // generate room number
    log("match found. sending room id " + roomId + " to users " + sessId + " and " + this.waitingUserSessId);
    smp.convertAndSend("/topic/ttt/waiting_room", roomId);
    this.waitingUserSessId = "";
  }

  /**
   * Creates a user session for the specified user and registers him to the
   * specified game.
   *
   * @param sessId   The session ID of the player.
   * @param roomId   The room ID of the game.
   * @param username The username of the player.
   * @param game     The Game instance in which the player is registered.
   */
  private void handleRegisterPlayer(String sessId, String roomId, String username, Game game) {
    UserSession userSession = this.sessIdToUserSession.get(sessId);
    userSession.setPlayerId(username);
    userSession.setRoomId(roomId);
    this.sessIdToUserSession.put(sessId, userSession);
    game.registerPlayer(username);
    log("user " + sessId + " joined room " + roomId);
  }

  /**
   * A wrapper for any logs related to the tictactoe game
   * 
   * @param log the text to be wrapped and logged
   */
  private void log(String log) {
    System.out.println("[tictactoe]: " + log);
  }

}