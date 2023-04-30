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

@Controller
public class TicTacToeController {

  private static final int ROOM_ID_LENGTH = 8;

  private String waitingUserSessId = "";
  private Map<String, Game> roomIdToGame = new HashMap<>();
  private Map<String, UserSession> sessIdToUserSession = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate smp;

  @MessageMapping("/ttt_room/{roomId}")
  public synchronized void command(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId,
      WebsocketCommand message) throws Exception {
    Game game = roomIdToGame.get(roomId);
    if (game == null) {
      game = new Game(smp, roomId);
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

  @EventListener
  public void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
    handleLeaveGame(event);
  }

  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    handleLeaveGame(event);
  }

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

  private String generateRoomId() {
    return UUID.randomUUID().toString().substring(0, ROOM_ID_LENGTH);
  }

  private void handleJoinWaitingRoom(StompHeaderAccessor headerAccessor) {
    String sessId = headerAccessor.getSessionId();
    if (this.waitingUserSessId.equals("")) {
      log("user " + sessId + " is waiting for a match");
      this.waitingUserSessId = sessId;
      return;
    }

    // a short wait to let the client establish the connection, otherwise the
    // message might drop
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

  private void handleRegisterPlayer(String sessId, String roomId, String username, Game game) {
    UserSession userSession = this.sessIdToUserSession.get(sessId);
    userSession.setPlayerId(username);
    userSession.setRoomId(roomId);
    this.sessIdToUserSession.put(sessId, userSession);
    game.registerPlayer(username);
    log("user " + sessId + " joined room " + roomId);
  }

  private void log(String log) {
    System.out.println("[tictactoe]: " + log);
  }

}