package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.AuthResponse;
import com.example.gym_saas_backend.dto.LoginRequest;
import com.example.gym_saas_backend.dto.RegisterRequest;
import com.example.gym_saas_backend.dto.UserAuthResult;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.service.AuthService;
import com.example.gym_saas_backend.service.RefreshTokenService;
import com.example.gym_saas_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            Owner owner = authService.registerOwner(request);
            if (owner == null) {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(false, "Registration failed", null, null, null)
                );
            }

            // Generate JWT Token valid for 21 days
            String accessToken = jwtUtil.generateToken(owner.getEmail(), "OWNER", owner.getId());

            // Save token to refresh_tokens table
            String refreshToken = UUID.randomUUID().toString();
            refreshTokenService.saveRefreshToken(refreshToken, owner.getId());// inject via @Autowired or constructor

            return ResponseEntity.status(201).body(
                    new AuthResponse(true, "Registration successful", accessToken, refreshToken, owner)
            );

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(new AuthResponse(false, "Registration failed: " + e.getMessage(), null, null, null));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            UserAuthResult result = authService.authenticateUser(request);
            String accessToken = jwtUtil.generateToken(result.getEmail(), result.getRole(), result.getId());

            String refreshToken = "";
            if(!result.getRole().equals("ADMIN")) {
                // Generate refresh token and store in DB
                refreshToken = UUID.randomUUID().toString(); // or use JWT for it too
                refreshTokenService.saveRefreshToken(refreshToken, result.getId());
            }
            else{
                refreshToken = "Tushar";
            }

            return ResponseEntity.ok(
                    new AuthResponse(true, "Login successful", accessToken, refreshToken, result.getOwner())
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(401)
                    .body(new AuthResponse(false, "Login failed: " + e.getMessage(), null, null, null));
        }
    }


}
