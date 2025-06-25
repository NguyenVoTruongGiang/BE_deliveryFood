package com.example.be_deliveryfood.controller;

import com.example.be_deliveryfood.config.JwtUtil;
import com.example.be_deliveryfood.entity.User;
import com.example.be_deliveryfood.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private UserService userService;

//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers(@RequestHeader(value = "Authorization", required = false) String token) {
//        System.out.println("GetAllUsers endpoint called with token: " + (token != null ? "provided" : "not provided"));
//        try {
//            if (token != null && token.startsWith("Bearer ")) {
//                String jwt = token.substring(7);
//                System.out.println("JWT: " + jwt.substring(0, Math.min(jwt.length(), 20)) + "...");
//                // Thêm log để xem JWT có hợp lệ không
//                try {
//                    String username = jwtUtil.getUsernameFromToken(jwt);
//                    String role = jwtUtil.getRoleFromToken(jwt);
//                    System.out.println("Username from token: " + username);
//                    System.out.println("Role from token: " + role);
//                } catch (Exception e) {
//                    System.out.println("Error parsing JWT: " + e.getMessage());
//                }
//            }
//
//            List<User> users = userService.getAllUsers();
//            System.out.println("Users found: " + users.size());
//            return ResponseEntity.ok(users);
//        } catch (Exception e) {
//            System.err.println("Error in getAllUsers: " + e.getMessage());
//            e.printStackTrace();
//            throw e;
//        }
//    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> orders = userService.getAllUsers();
        return ResponseEntity.ok(orders);
    }



    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
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

    @GetMapping("/search/name")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.searchUsersByName(name));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable User.Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @GetMapping("/count/role/{role}")
    public ResponseEntity<Long> countUsersByRole(@PathVariable User.Role role) {
        return ResponseEntity.ok(userService.countUsersByRole(role));
    }
}