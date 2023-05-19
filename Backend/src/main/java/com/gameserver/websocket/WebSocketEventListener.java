package com.gameserver.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Event listener for WebSocket events.
 */
@Component
public class WebSocketEventListener {
    /**
     * Logs WebSocket connect events.
     * 
     * @param event the SessionConnectEvent
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        System.out.println("WebSocket connected");
    }

    /**
     * Logs WebSocket disconnect events.
     * 
     * @param event the SessionDisconnectEvent
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("WebSocket disconnected");
    }
}
