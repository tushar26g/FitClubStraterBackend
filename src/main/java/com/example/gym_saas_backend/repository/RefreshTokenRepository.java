package com.example.gym_saas_backend.repository;

import com.example.gym_saas_backend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenAndOwnerId(String token, Long ownerId);
    void deleteByToken(String token);

    @Query("SELECT m FROM RefreshToken m WHERE m.ownerId = :gymOwnerId AND (m.token = :token)")
    Optional<RefreshToken> findByTokenAndGymOwnerId(String token, Long gymOwnerId);
}

