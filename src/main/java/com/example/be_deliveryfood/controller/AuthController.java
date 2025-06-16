package com.example.be_deliveryfood.controller;

import com.example.be_deliveryfood.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String oldToken = request.get("token");
        if (oldToken == null || !jwtUtil.validateToken(oldToken)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid token"));
        }
        String username = jwtUtil.getUsernameFromToken(oldToken);
        String newToken = jwtUtil.generateToken(username);
        Map<String, String> response = new HashMap<>();
        response.put("token", newToken);
        return ResponseEntity.ok(response);
    }
}