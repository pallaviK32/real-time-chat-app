package com.pallavi.messaging_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.stereotype.Component;


import java.security.Principal;
import java.util.Map;


@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        String query = request.getURI().getQuery(); // e.g., "token=xxx&t=12345"
        if (query != null) {
            String token = null;

            // Split the query string by '&' to get all params
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    token = param.substring("token=".length());
                    break;
                }
            }

            if (token != null) {
                System.out.println("✅ Token found in query: " + token);

                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    if (username != null && !username.isEmpty()) {
                        attributes.put("username", username);
                        attributes.put("token", token);  // store raw token too
                        attributes.put("principal", new StompPrincipal(username));
                        System.out.println("✅ Created Principal for: " + username);
                        return true;
                    } else {
                        System.out.println("❌ Username extraction failed from token");
                        return false;
                    }
                } else {
                    System.out.println("❌ Invalid JWT token");
                    return false;
                }
            } else {
                System.out.println("❌ No token found in query parameters");
                return false;
            }
        } else {
            System.out.println("❌ No query string present");
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // no-op
    }
}