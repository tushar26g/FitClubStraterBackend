package com.example.gym_saas_backend.dto;

import com.example.gym_saas_backend.entity.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    private String token; // null if registration fails
    private Owner owner;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public AuthResponse(boolean success, String message, String token, Owner owner) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.owner = owner;
    }
}
