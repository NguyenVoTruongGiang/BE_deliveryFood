package com.example.be_deliveryfood.dto.request;
import lombok.Data;

@Data
public class SignupRequest {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}