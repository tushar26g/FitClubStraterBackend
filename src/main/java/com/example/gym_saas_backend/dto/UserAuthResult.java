package com.example.gym_saas_backend.dto;

public class UserAuthResult {
    private String email;
    private String role;
    private Long id;

    public UserAuthResult(String email, String role, Long id) {
        this.email = email;
        this.role = role;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
