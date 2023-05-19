package com.gameserver.websocket;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for WebSocket messaging.
 */
@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages = "com.jpa.JPA.websocket")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  /**
   * Configures the message broker.
   * 
   * @param config the MessageBrokerRegistry instance
   */
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
  }

  /**
   * 
   * Registers the STOMP endpoints.
   * 
   * @param registry the StompEndpointRegistry instance
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
  }

}