package com.example.be_deliveryfood.config;

import com.example.be_deliveryfood.dto.repository.*;
import com.example.be_deliveryfood.entity.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DataSeeder(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            PasswordEncoder passwordEncoder 
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Users
        if (userRepository.count() == 0) {
            InputStream userStream = new ClassPathResource("data/users.json").getInputStream();
            List<User> users = mapper.readValue(userStream, new TypeReference<List<User>>() {});
            for (User user : users) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.saveAll(users);
            System.out.println("Seeded users from JSON!");
        }

        // Seed Products
        if (productRepository.count() == 0) {
            try {
                InputStream productStream = new ClassPathResource("data/products.json").getInputStream();
                List<Product> products = mapper.readValue(productStream, new TypeReference<List<Product>>() {});
                productRepository.saveAll(products);
                System.out.println("Seeded products from JSON!");
            } catch (Exception e) {
                System.out.println("Failed to seed products: " + e.getMessage());
            }
        }
    }
}