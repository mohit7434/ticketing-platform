package com.ticketing.authservice.config;

import com.ticketing.authservice.entity.User;
import com.ticketing.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if the admin user already exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin123!"));

            // Set the role string exact to what your JWT expects: "ROLE_ADMIN"
            admin.setRole("ROLE_ADMIN");

            userRepository.save(admin);
            System.out.println(">>> Seeded default admin user into database: admin / Admin123!");
        }
    }
}