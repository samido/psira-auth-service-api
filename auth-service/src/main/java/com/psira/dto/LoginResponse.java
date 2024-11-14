package com.psira.dto;

import com.psira.entity.Role;

public class LoginResponse {
    private String token;
    private String message;
    private Role role;
    private String username;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LoginResponse(String token, String message, Role role, String username) {
        this.token = token;
        this.message = message;
        this.role = role;
        this.username = username;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}