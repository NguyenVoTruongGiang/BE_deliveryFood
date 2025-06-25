package com.example.be_deliveryfood.dto.repository;

import com.example.be_deliveryfood.entity.ChatMessage;
import com.example.be_deliveryfood.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            User sender, User receiver, User receiver2, User sender2);
}