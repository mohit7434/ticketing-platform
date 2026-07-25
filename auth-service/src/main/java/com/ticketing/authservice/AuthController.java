package com.ticketing.authservice;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterRequest request) {
        return "User " + request.getUsername() + " registered successfully!";
    }

    // Simple DTO class for request body
    public static class RegisterRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}