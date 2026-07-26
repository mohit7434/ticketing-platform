package com.ticketing.authservice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Encrypt password with BCrypt
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getUsername(), hashedPassword);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully in H2! User ID: " + user.getId());
    }

    public static class RegisterRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}