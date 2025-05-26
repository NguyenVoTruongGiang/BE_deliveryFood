package com.example.be_deliveryfood.service;

import com.example.be_deliveryfood.config.JwtUtil;
import com.example.be_deliveryfood.dto.request.LoginRequest;
import com.example.be_deliveryfood.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.be_deliveryfood.dto.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>()
        );
    }

    public String login(LoginRequest loginRequest) throws Exception {
        try {
            Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
            if (user.isPresent()) {
                User foundUser = user.get();
                if (!foundUser.getActive()) {
                    System.out.println("Login failed: User is inactive");
                    throw new ValidationException("User account is inactive");
                }
                boolean passwordMatch = passwordEncoder.matches(loginRequest.getPassword(), foundUser.getPassword());
                if (!passwordMatch) {
                    System.out.println("Login failed: Password does not match for email " + loginRequest.getEmail());
                    throw new ValidationException("Invalid credentials");
                }
                System.out.println("Login success for email: " + loginRequest.getEmail());
                return jwtUtil.generateToken(foundUser.getEmail());
            }
            System.out.println("Login failed: Email not found - " + loginRequest.getEmail());
            throw new ValidationException("Invalid credentials");
        } catch (Exception e) {
            System.out.println("Exception in login: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("User not found"));
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new ValidationException("Email already exists");
            }
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }
        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }
        if (userDetails.getActive() != null) {
            user.setActive(userDetails.getActive());
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ValidationException("User not found");
        }
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    public List<User> getUsersByRoleAndActive(User.Role role, boolean active) {
        return userRepository.findByRoleAndActive(role, active);
    }

    public long countUsersByRole(User.Role role) {
        return userRepository.countByRole(role);
    }

    public List<User> getAllUsersSortedByName() {
        return userRepository.findAllByOrderByNameAsc();
    }
}