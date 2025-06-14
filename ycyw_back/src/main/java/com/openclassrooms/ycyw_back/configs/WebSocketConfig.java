package com.openclassrooms.ycyw_back.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /*
         * Handles the diffusion of the messages to all clients subscribed to '/topic/chat.{chatId}'.
         * topic = spring convention for 'broadcast' communication.
         */
        config.enableSimpleBroker("/topic");
        // Handles the messages sent to the server.
        config.setApplicationDestinationPrefixes("/ws");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /*
         * https://www.dariawan.com/tutorials/spring/spring-boot-websocket-stomp-tutorial/
         * "STOMP describes the message format exchanged between clients and servers.
         * On another hand, WebSocket is nothing but a communication protocol."
         */
        registry.addEndpoint("/ws/chat")
                .setAllowedOrigins("http://localhost:4200")
                /* SockJs is used as a fallback option for older browsers with no WebSocket support.
                 * (-> long polling. Also handle connections failures, heartbeat, etc.)
                */
                .withSockJS();
    }
}