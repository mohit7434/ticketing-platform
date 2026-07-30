package com.ticketing.authservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Good practice to avoid keyword collisions
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role = "ROLE_USER"; // Default value set here
    // 1. Default No-Args Constructor (Required by JPA/Hibernate)
    public User() {
    }
    // 2. Constructor without role (Defaults to "ROLE_USER")
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "ROLE_USER";
    }
    // 3. Constructor with role (Useful if creating an ADMIN user)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role != null ? role : "ROLE_USER";
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}