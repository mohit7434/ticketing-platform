package com.ticketing.authservice.service;

import com.ticketing.authservice.dto.AuthResponse;
import com.ticketing.authservice.dto.LoginRequest;
import com.ticketing.authservice.dto.RegisterRequest;
import com.ticketing.authservice.dto.TokenRefreshRequest;
import com.ticketing.authservice.entity.RefreshToken;
import com.ticketing.authservice.entity.User;
import com.ticketing.authservice.exception.InvalidCredentialsException;
import com.ticketing.authservice.exception.UserAlreadyExistsException;
import com.ticketing.authservice.repository.UserRepository;
import com.ticketing.authservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    // 1. REGISTER
    public String register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "User registered successfully";
    }

    // 2. LOGIN
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String accessToken = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    // 3. REFRESH TOKEN
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
                    return new AuthResponse(newAccessToken, request.getRefreshToken());
                })
                .orElseThrow(() -> new InvalidCredentialsException("Refresh token is not present in database!"));
    }
}