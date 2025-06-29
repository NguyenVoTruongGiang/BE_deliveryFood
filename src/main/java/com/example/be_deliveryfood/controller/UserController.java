package com.example.be_deliveryfood.controller;

import com.example.be_deliveryfood.config.JwtUtil;
import com.example.be_deliveryfood.dto.request.LoginRequest;
import com.example.be_deliveryfood.entity.Order;
import com.example.be_deliveryfood.entity.User;
import com.example.be_deliveryfood.service.EmailService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.be_deliveryfood.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> orders = userService.getAllUsers();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        Optional<User> userOpt = userService.getUserByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            throw new ValidationException("User not found");
        }
        User user = userOpt.get();
        String token = userService.login(loginRequest);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Login successful");
        response.put("name", user.getName());
        response.put("user_id", user.getId().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login/success")
    public ResponseEntity<Map<String, String>> loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed, principal is null"));
        }
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Email not found in OAuth2 response"));
        }
        User user = userService.handleOAuth2User(email, name);
        String token = jwtUtil.generateToken(email, user.getRole() != null ? user.getRole().name() : "CUSTOMER");
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Login successful");
        response.put("name", user.getName());
        response.put("user_id", user.getId().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    // Gửi OTP cho đăng ký tài khoản
    @PostMapping("/send-register-otp")
    public ResponseEntity<Map<String, String>> sendRegisterOtp(@RequestBody Map<String, String> request) {
        String to = request.get("to");
        String otp = emailService.sendRegisterOtp(to);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Verification OTP sent");
        // response.put("otp", otp); // chỉ trả về nếu muốn test, thực tế không nên trả về OTP cho client
        return ResponseEntity.ok(response);
    }

    // Gửi OTP cho quên mật khẩu
    @PostMapping("/send-forgot-password-otp")
    public ResponseEntity<Map<String, String>> sendForgotPasswordOtp(@RequestBody Map<String, String> request) {
        String to = request.get("to");
        String otp = emailService.sendForgotPasswordOtp(to);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Reset password OTP sent");
        // response.put("otp", otp); // chỉ trả về nếu muốn test
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable User.Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @GetMapping("/role/{role}/active/{active}")
    public ResponseEntity<List<User>> getUsersByRoleAndActive(@PathVariable User.Role role, @PathVariable boolean active) {
        return ResponseEntity.ok(userService.getUsersByRoleAndActive(role, active));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.searchUsersByName(name));
    }

    @GetMapping("/count/role/{role}")
    public ResponseEntity<Long> countUsersByRole(@PathVariable User.Role role) {
        return ResponseEntity.ok(userService.countUsersByRole(role));
    }
}
