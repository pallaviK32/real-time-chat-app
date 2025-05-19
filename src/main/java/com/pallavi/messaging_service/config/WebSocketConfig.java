package com.pallavi.messaging_service.config;

import com.pallavi.messaging_service.security.WebSocketAuthChannelInterceptor;
import com.pallavi.messaging_service.security.JwtHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketAuthChannelInterceptor authChannelInterceptor;

    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/user");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(handshakeHandler()) // ⬅️ add this
//                .setAllowedOriginPatterns("*")
                  .setAllowedOriginPatterns("*")
//                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request,
                                              WebSocketHandler wsHandler,
                                              Map<String, Object> attributes) {
                String username = (String) attributes.get("username");
                System.out.println("✅ Creating Principal for: " + username);
                return username != null ? () -> username : null;
            }
        };
    }

}