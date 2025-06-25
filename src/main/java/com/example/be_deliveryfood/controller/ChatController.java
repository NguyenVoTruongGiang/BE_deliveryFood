package com.example.be_deliveryfood.controller;

import com.example.be_deliveryfood.dto.request.ChatRequest;
import com.example.be_deliveryfood.entity.ChatMessage;
import com.example.be_deliveryfood.entity.User;
import com.example.be_deliveryfood.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

//    @PostMapping("/send")
//    public ChatMessage sendMessage(@RequestBody ChatRequest request, @AuthenticationPrincipal UserDetails userDetails) {
//        Long senderId = chatService.getUserById(Long.parseLong(userDetails.getUsername())).getId();
//        return chatService.sendMessage(request, senderId);
//    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatRequest request, Authentication authentication) {
        ChatMessage message = chatService.sendMessage(request, request.getSenderId());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return chatService.getChatHistory(senderId, receiverId);
    }
}