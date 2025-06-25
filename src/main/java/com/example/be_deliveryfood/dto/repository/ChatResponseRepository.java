package com.example.be_deliveryfood.dto.repository;

import com.example.be_deliveryfood.entity.ChatResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatResponseRepository extends JpaRepository<ChatResponse, Long> {
    Optional<ChatResponse> findByKeyword(String keyword);
    List<ChatResponse> findByIsSuggestionFalse();
}