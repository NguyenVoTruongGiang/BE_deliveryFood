package com.example.be_deliveryfood.config;

import com.example.be_deliveryfood.dto.request.ChatRequest;
import com.example.be_deliveryfood.entity.ChatMessage;
import com.example.be_deliveryfood.entity.User;
import com.example.be_deliveryfood.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private ChatService chatService;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("token=")) {
            String token = query.split("token=")[1];
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret) // Sử dụng khóa từ properties
                        .parseClaimsJws(token)
                        .getBody();
                String userEmail = claims.getSubject();
                session.getAttributes().put("userEmail", userEmail);
                sessions.put(session.getId(), session);
                session.sendMessage(new TextMessage("{\"type\":\"auth_success\"}"));
            } catch (Exception e) {
                session.close(CloseStatus.BAD_DATA.withReason("Invalid token"));
            }
        } else {
            session.close(CloseStatus.BAD_DATA.withReason("No token provided"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
        String type = (String) payload.get("type");

        if ("chat".equals(type)) {
            Long senderId = Long.valueOf(payload.get("senderId").toString());
            Long receiverId = Long.valueOf(payload.get("receiverId").toString());
            String content = (String) payload.get("content");

            ChatRequest chatRequest = new ChatRequest();
            chatRequest.setSenderId(senderId);
            chatRequest.setReceiverId(receiverId);
            chatRequest.setContent(content);

            ChatMessage chatMessage = chatService.sendMessage(chatRequest, senderId);

            Map<String, Object> response = Map.of(
                    "id", chatMessage.getId(),
                    "content", chatMessage.getContent(),
                    "sender", Map.of("email", chatMessage.getSender().getEmail()),
                    "timestamp", chatMessage.getTimestamp().toString()
            );
            String responseJson = objectMapper.writeValueAsString(response);

            for (WebSocketSession s : sessions.values()) {
                if (s.isOpen()) {
                    String sessionUserEmail = s.getAttributes().getOrDefault("userEmail", "").toString();
                    User sender = chatService.getUserById(senderId);
                    User receiver = chatService.getUserById(receiverId);
                    if (sessionUserEmail.equals(sender.getEmail()) || sessionUserEmail.equals(receiver.getEmail())) {
                        s.sendMessage(new TextMessage(responseJson));
                    }
                }
            }
        } else if ("ping".equals(type)) {
            session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }
}