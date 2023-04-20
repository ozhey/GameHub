package com.gameserver.snake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class SnakeController {

  @Autowired
  private SimpMessagingTemplate messagingTemplate;
  
    @MessageMapping("/hello")
    @SendTo("/topic/snake")
    public void greeting(String message) throws Exception {
        System.out.println(message);
    }

  }