package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.entity.RefreshToken;

public interface RefreshTokenService {
    void saveRefreshToken(String token, Long ownerId);
    boolean isValid(String token, Long ownerId);
    void deleteToken(String token);
    boolean validateAndDelete(String token, Long gymOwnerId);
    String generateResetToken(Owner owner);
    RefreshToken validateResetToken(String tokenStr);
    void invalidateResetToken(String tokenStr);
}
