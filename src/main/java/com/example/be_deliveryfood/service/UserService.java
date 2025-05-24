package com.example.be_deliveryfood.service;


import com.example.be_deliveryfood.config.JwtService;
import com.example.be_deliveryfood.dto.request.LoginRequest;
import com.example.be_deliveryfood.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import com.example.be_deliveryfood.dto.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

//    public String login(LoginRequest loginRequest) throws Exception {
//        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
//        if (user.isPresent()) {
//            if (!user.get().getActive()) {
//                throw new ValidationException("User account is inactive");
//            }
//            return jwtService.generateToken(user.get());
//        }
//        throw new ValidationException("Invalid credentials");
//    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Email already exists");
        }
        user.setPassword((user.getPassword()));
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