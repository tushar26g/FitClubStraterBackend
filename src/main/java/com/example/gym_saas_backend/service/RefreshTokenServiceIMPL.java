package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.entity.RefreshToken;
import com.example.gym_saas_backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RefreshTokenServiceIMPL implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repository;

    @Override
    public void saveRefreshToken(String token, Long ownerId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setOwnerId(ownerId);
        refreshToken.setCreatedAt(LocalDateTime.now());
        repository.save(refreshToken);
    }

    @Override
    public boolean isValid(String token, Long ownerId) {
        Optional<RefreshToken> opt = repository.findByTokenAndOwnerId(token, ownerId);
        return opt.isPresent() && opt.get().getCreatedAt().isAfter(LocalDateTime.now().minusDays(21));
    }

    @Override
    public void deleteToken(String token) {
        repository.deleteByToken(token);
    }

    public boolean validateAndDelete(String token, Long gymOwnerId) {
        Optional<RefreshToken> refreshTokenOpt = repository.findByTokenAndGymOwnerId(token, gymOwnerId);
        if (refreshTokenOpt.isPresent()) {
            repository.delete(refreshTokenOpt.get());
            return true;
        }
        return false;
    }

}

