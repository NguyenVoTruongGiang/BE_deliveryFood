package com.example.be_deliveryfood.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chat_responses")
public class ChatResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(nullable = false, unique = true, length = 255, name = "keyword")
    private String keyword;

    @Column(nullable = false, length = 1000, name = "response")
    private String response;

    @Column(nullable = false, name = "is_suggestion")
    @JsonProperty("is_suggestion")
    private boolean isSuggestion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuggestion() {
        return isSuggestion;
    }

    public void setSuggestion(boolean suggestion) {
        isSuggestion = suggestion;
    }
}