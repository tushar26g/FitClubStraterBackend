package com.example.gym_saas_backend.dto;

import com.example.gym_saas_backend.entity.Owner;

public class RefreshTokenRequest {
    private String refreshToken;
    private Owner gymOwner;
    private String emailId;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Owner getGymOwner() {
        return gymOwner;
    }

    public void setGymOwner(Owner gymOwner) {
        this.gymOwner = gymOwner;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
