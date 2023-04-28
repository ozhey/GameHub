package com.gameserver.tictactoe;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.gameserver.tictactoe.models.Game;
import com.gameserver.tictactoe.models.Move;

@Controller
public class TicTacToeController {

  private static final int ROOM_ID_LENGTH = 8;

  private String waitingUserSessId = "";
  private Map<String, Game> roomIdToGame = new HashMap<>();
  private Map<String, String> sessIdToRoomId = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate smp;

  @MessageMapping("/ttt_room/{roomId}")
  public void command(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId, Move message)
      throws Exception {
    Game game = roomIdToGame.get(roomId);
    if (game == null) {
      return;
    }
    
    if (message.getCommand().equals("start")) {
      game.resetGame();
    } else if (message.getCommand().equals("move")) {
      game.makeMove(message.getRow(), message.getCol());
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
      return;
    }

    // subscribed to a specific game room
    Pattern pattern = Pattern.compile("^/topic/ttt_room/[0-9a-f]{" + ROOM_ID_LENGTH + "}$");
    Matcher matcher = pattern.matcher(dest);
    if (matcher.matches()) {
      handleJoinGameRoom(headerAccessor, dest);
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

  private void handleLeaveGame(AbstractSubProtocolEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessId = headerAccessor.getSessionId();

    // if user did not play tic tac toe, return
    if (!(this.waitingUserSessId.equals(sessId)) && !this.sessIdToRoomId.containsKey(sessId)) {
      return;
    }
    
    if (this.waitingUserSessId.equals(sessId)) {
      this.waitingUserSessId = "";
      log("user " + sessId + "left the waiting room");
    }

    String roomId = this.sessIdToRoomId.get(sessId);
    if (roomId != null && this.roomIdToGame.containsKey(roomId)) { // player left an active game
      log("user " + sessId + " left room " + roomId + ". letting the other player know and deleting game instance");
      this.smp.convertAndSend("/topic/ttt_room/" + roomId, "end");
      this.roomIdToGame.remove(roomId);
      this.sessIdToRoomId.remove(roomId);
    }

    if (roomId != null) { // player left a game but he was already alone
      log("user " + sessId + " left room " + roomId);
      this.sessIdToRoomId.remove(roomId);
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
    } else {
      String roomId = generateRoomId(); // generate room number

      log("match found. sending room id " + roomId + " to users " + sessId + " and " + this.waitingUserSessId);
      smp.convertAndSend("/topic/ttt/waiting_room", roomId);
      this.waitingUserSessId = "";
    }
  }

  private void handleJoinGameRoom(StompHeaderAccessor headerAccessor, String dest) {
    String sessId = headerAccessor.getSessionId();
    String roomId = dest.substring(dest.length() - ROOM_ID_LENGTH);

    // player joined an existing room
    if (this.roomIdToGame.containsKey(roomId)) {
      this.sessIdToRoomId.put(sessId, roomId);
      log("user " + sessId + " joined room " + roomId);
      return;
    }

    // player is the first player in the room, create a new game
    this.sessIdToRoomId.put(sessId, roomId);
    this.roomIdToGame.put(roomId, new Game(smp, roomId));
    log("game room created. room id: " + roomId + " user: " + sessId);
  }

  private void log(String log) {
    System.out.println("[tictactoe]: " + log);
  }

}