package com.ticketing.authservice.service;

import com.ticketing.authservice.dto.AuthResponse;
import com.ticketing.authservice.dto.LoginRequest;
import com.ticketing.authservice.entity.User;
import com.ticketing.authservice.repository.UserRepository;
import com.ticketing.authservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Inject dependencies via constructor
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Fetch user from Database
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // 2. Verify hashed password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 3. Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(),user.getRole());

        return new AuthResponse(token);
    }
}