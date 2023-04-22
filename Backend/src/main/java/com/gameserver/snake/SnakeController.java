package com.gameserver.snake;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Controller
public class SnakeController {

  private Map<String, GameState> gameStateMap = new HashMap<>();
  private Map<String, String> subIdToRoomId = new HashMap<>();
  private Map<String, Integer> numOfPlayersInRoom = new HashMap<>();

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/snake_room/{roomId}")
  public void command(@DestinationVariable String roomId, String message) throws Exception {
    int playersCount = numOfPlayersInRoom.getOrDefault(roomId, 0);
    switch (message) {
      case "start":
        GameState gameState = gameStateMap.get(roomId);
        if (gameState == null) {
          gameState = new GameState(messagingTemplate, playersCount, roomId);
          gameStateMap.put(roomId, gameState);
        }
        gameState.start(playersCount);
        messagingTemplate.convertAndSend("/topic/snake_room/" + roomId, gameState);
        break;

      default:
        break;
    }
  }

  @EventListener
  public void handleSessionSubscribe(SessionSubscribeEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessId = headerAccessor.getSessionId();
    String roomId = extractRoomIdFromDestination(headerAccessor.getDestination());
    subIdToRoomId.put(sessId, roomId);

    int numOfPlayers = numOfPlayersInRoom.getOrDefault(roomId, 0);
    numOfPlayersInRoom.put(roomId, ++numOfPlayers);

    System.out.println(numOfPlayers);
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
    String roomId = subIdToRoomId.get(sessId);
    subIdToRoomId.remove(sessId);

    int numOfPlayers = numOfPlayersInRoom.getOrDefault(roomId, 0);
    numOfPlayersInRoom.put(roomId, --numOfPlayers);

    // if a game is in progress and there are no players, kill room
    if (numOfPlayers <= 0 && gameStateMap.containsKey(roomId)) {
      gameStateMap.get(roomId).resetTimer();
      gameStateMap.remove(roomId);
    }

    System.out.println(numOfPlayers);
  }

}