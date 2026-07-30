package com.ticketing.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // Secret key for signing the token (must be at least 256 bits / 32 characters long)
    private final String SECRET_STRING = "your-super-secret-32-byte-long-key-string-for-jwt-signing!";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    // Token expiration time: 1 hour in milliseconds
    private final long EXPIRATION_TIME = 3600000;

    // 1. Generate JWT Token with Claims
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // 2. Validate Token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 3. Extract Username from Token
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // 4. Extract Role from Token
    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Helper method to parse Claims
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}