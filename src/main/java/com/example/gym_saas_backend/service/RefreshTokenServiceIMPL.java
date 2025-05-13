package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.entity.RefreshToken;
import com.example.gym_saas_backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public String generateResetToken(Owner owner) {
        String tokenStr = UUID.randomUUID().toString();

        RefreshToken token = new RefreshToken();
        token.setToken(tokenStr);
        token.setOwnerId(owner.getId());
        token.setTokenType("RESET");
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        repository.save(token);
        return tokenStr;
    }

    @Override
    public RefreshToken validateResetToken(String tokenStr) {
        RefreshToken token = repository.findByTokenAndTokenType(tokenStr, "RESET")
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        return token;
    }

    @Override
    @Transactional // ✅ Required for delete to work
    public void invalidateResetToken(String tokenStr) {
        if (tokenStr == null || tokenStr.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        repository.deleteByToken(tokenStr);
    }

}

