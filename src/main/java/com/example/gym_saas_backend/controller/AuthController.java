package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.AuthResponse;
import com.example.gym_saas_backend.dto.LoginRequest;
import com.example.gym_saas_backend.dto.RegisterRequest;
import com.example.gym_saas_backend.dto.UserAuthResult;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.service.AuthService;
import com.example.gym_saas_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            Owner owner = authService.registerOwner(request);
            if (owner == null) {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(false, "Registration failed", null)
                );
            }

            String token = jwtUtil.generateToken(owner.getEmail(), "OWNER", owner.getId());
            return ResponseEntity.status(201).body(
                    new AuthResponse(true, "Registration successful", token)
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(new AuthResponse(false, "Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            // returns object, role, and id
            UserAuthResult result = authService.authenticateUser(request);
            String token = jwtUtil.generateToken(result.getEmail(), result.getRole(), result.getId());

            return ResponseEntity.ok(
                    new AuthResponse(true, "Login successful", token)
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(401)
                    .body(new AuthResponse(false, "Login failed: " + e.getMessage(), null));
        }
    }

}
