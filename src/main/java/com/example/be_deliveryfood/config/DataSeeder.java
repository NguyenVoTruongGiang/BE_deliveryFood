package com.example.be_deliveryfood.config;

import com.example.be_deliveryfood.entity.User;
import com.example.be_deliveryfood.dto.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("data/users.json").getInputStream();
            List<User> users = mapper.readValue(inputStream, new TypeReference<List<User>>() {});
            userRepository.saveAll(users);
            System.out.println("Seeded users from JSON!");
        }
    }
}