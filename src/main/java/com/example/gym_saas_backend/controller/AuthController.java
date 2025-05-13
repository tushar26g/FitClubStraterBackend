package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.*;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.entity.RefreshToken;
import com.example.gym_saas_backend.repository.OwnerRepository;
import com.example.gym_saas_backend.service.AuthService;
import com.example.gym_saas_backend.service.RefreshTokenService;
import com.example.gym_saas_backend.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.NoSuchElementException;
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

    @Value("${app.send.excel.to}")
    private String recipientEmail;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestPart("dto") RegisterRequest dto,
                                                 @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
        try {
            Owner owner = authService.registerOwner(dto, profilePhoto);
            if (owner == null) {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(false, "Registration failed", null, null, null)
                );
            }

            // Generate JWT Token valid for 21 days
            String accessToken = jwtUtil.generateToken(owner.getMobileNumber(), "OWNER", owner.getId());

            // Save token to refresh_tokens table
            String refreshToken = UUID.randomUUID().toString();
//            refreshTokenService.saveRefreshToken(refreshToken, owner.getId());// inject via @Autowired or constructor

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
            String accessToken = jwtUtil.generateToken(result.getOwner().getMobileNumber(), result.getRole(), result.getId());

            String refreshToken = "";
            if(!result.getRole().equals("ADMIN")) {
                // Generate refresh token and store in DB
                refreshToken = UUID.randomUUID().toString(); // or use JWT for it too
//                refreshTokenService.saveRefreshToken(refreshToken, result.getId());
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

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken1 = request.getRefreshToken();
        Owner gymOwner = request.getGymOwner();
        Long gymOwnerId = gymOwner.getId();
        boolean isValid = refreshTokenService.validateAndDelete(refreshToken1, gymOwnerId);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Invalid or expired refresh token", null, null, null));
        }

        // Generate new access token
        String email = request.getEmailId();
        String role = "OWNER";
        String accessToken = jwtUtil.generateToken(email, role, gymOwnerId);

        // Generate new refresh token and store it
        String refreshToken = UUID.randomUUID().toString();
        refreshTokenService.saveRefreshToken(refreshToken, gymOwnerId);

        return ResponseEntity.ok(
                new AuthResponse(true, "Token refreshed", accessToken, refreshToken, gymOwner)
        );
    }

    @PostMapping("/contact")
    public String sendContactQuery(@RequestBody ContactRequest request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject("New Contact Query from Website");
        helper.setText(
                "Name: " + request.getName() + "\n" +
                        "Mobile Number: " + request.getMobileNumber() + "\n" +
                        "Query: " + request.getQuery(),
                false
        );

        mailSender.send(message);
        return "Message sent";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.processForgotPassword(request.getMobileNumber());
        return ResponseEntity.ok(Map.of("message", "Reset password link sent to your registered email."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        RefreshToken token = refreshTokenService.validateResetToken(request.getToken());

        Owner owner = ownerRepository.findById(token.getOwnerId())
                .orElseThrow(() -> new NoSuchElementException("Owner not found"));

        refreshTokenService.invalidateResetToken(request.getToken());

        owner.setPassword(passwordEncoder.encode(request.getNewPassword()));
        ownerRepository.save(owner);

        return ResponseEntity.ok(Map.of("message", "Password reset successful."));
    }


}
