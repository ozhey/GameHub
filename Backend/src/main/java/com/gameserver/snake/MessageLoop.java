package com.gameserver.snake;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageLoop {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageLoop(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
}