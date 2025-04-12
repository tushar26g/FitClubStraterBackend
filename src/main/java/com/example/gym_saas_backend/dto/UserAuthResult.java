package com.example.gym_saas_backend.dto;

import com.example.gym_saas_backend.entity.Owner;

public class UserAuthResult {
    private String email;
    private String role;
    private Long id;
    private Owner owner;

    public UserAuthResult(String email, String role, Long id, Owner owner) {
        this.email = email;
        this.role = role;
        this.id = id;
        this.owner = owner;
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
