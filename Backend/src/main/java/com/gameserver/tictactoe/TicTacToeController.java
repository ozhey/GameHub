package com.gameserver.tictactoe;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
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

  private String waitingUserSessId = "";
  private Map<String, Game> roomIdToGame = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/ttt_room/{roomId}")
  public void command(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId, Move message)
      throws Exception {

    if (message.getCommand().equals("start")) {

    } else if (message.getCommand().equals("move")) {

    }
  }

  @EventListener
  public synchronized void handleSessionSubscribe(SessionSubscribeEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    if (headerAccessor.getDestination() != null &&
        !headerAccessor.getDestination().startsWith("/topic/ttt")) { // player subscribed to other game
      return;
    }

    String sessId = headerAccessor.getSessionId();

    if (this.waitingUserSessId.equals("")) {
      this.waitingUserSessId = sessId;
    } else {
      String roomNumber = generateRoomId(); // generate room number
      System.out.println("sending tictactoe room id to " + sessId + " and " + this.waitingUserSessId);
      messagingTemplate.convertAndSend("/topic/ttt/waiting_room", roomNumber);
      this.waitingUserSessId = "";
    }
    
    System.out.println("currently waiting: " + (this.waitingUserSessId.equals("") ? "no one" : this.waitingUserSessId));
  }

  @EventListener
  public void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {

  }

  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {

  }

  private String generateRoomId() {
    UUID uuid = UUID.randomUUID();
    String roomId = uuid.toString().substring(0, 8);
    return roomId;
  }

}