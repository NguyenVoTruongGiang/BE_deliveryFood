package com.example.be_deliveryfood.service;

import com.example.be_deliveryfood.dto.request.ChatRequest;
import com.example.be_deliveryfood.entity.ChatMessage;
import com.example.be_deliveryfood.entity.ChatResponse;
import com.example.be_deliveryfood.entity.User;
import com.example.be_deliveryfood.dto.repository.ChatMessageRepository;
import com.example.be_deliveryfood.dto.repository.ChatResponseRepository;
import com.example.be_deliveryfood.dto.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatResponseRepository chatResponseRepository;

    @Autowired
    private UserRepository userRepository;

    public ChatMessage sendMessage(ChatRequest request, Long senderId) {
        // Validate sender
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found: " + senderId));

        Long receiverId = 2L;
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found: " + receiverId));

        // Save user message
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSender(sender);
        userMessage.setReceiver(receiver);
        userMessage.setContent(request.getContent());
        ChatMessage savedUserMessage = chatMessageRepository.save(userMessage);

        // Use array to store admin reply (mutable in lambda)
        final String[] adminReply = new String[1];

        // Check for keyword match in the sentence
        if (adminReply[0] == null) {
            List<ChatResponse> keywordResponses = chatResponseRepository.findByIsSuggestionFalse();
            String lowercaseContent = request.getContent().toLowerCase();
            for (ChatResponse response : keywordResponses) {
                String lowercaseKeyword = response.getKeyword().toLowerCase();
                if (lowercaseContent.contains(lowercaseKeyword)) {
                    adminReply[0] = response.getResponse();
                    break; // Take the first matching keyword
                }
            }
        }

        // Save admin auto-response if applicable
        if (adminReply[0] != null) {
            ChatMessage adminMessage = new ChatMessage();
            adminMessage.setSender(receiver); // Admin (sender_id = 2)
            adminMessage.setReceiver(sender); // User
            adminMessage.setContent(adminReply[0]);
            chatMessageRepository.save(adminMessage);
        }

        return savedUserMessage;
    }

    public List<ChatMessage> getChatHistory(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found: " + senderId));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found: " + receiverId));
        return chatMessageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
                sender, receiver, sender, receiver);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }
}