package com.example.gym_saas_backend.service;

public interface RefreshTokenService {
    void saveRefreshToken(String token, Long ownerId);
    boolean isValid(String token, Long ownerId);
    void deleteToken(String token);
}
