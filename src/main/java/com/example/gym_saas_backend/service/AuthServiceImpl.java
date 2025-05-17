package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.LoginRequest;
import com.example.gym_saas_backend.dto.RegisterRequest;
import com.example.gym_saas_backend.dto.UserAuthResult;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.entity.Staff;
import com.example.gym_saas_backend.other.ImageCompressor;
import com.example.gym_saas_backend.repository.OwnerRepository;
import com.example.gym_saas_backend.repository.StaffRepository;
import com.example.gym_saas_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public Owner registerOwner(RegisterRequest request, MultipartFile profilePhoto) {
        if (request.getEmail() != null && ownerRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new RuntimeException("Email already registered");
        }

        if (ownerRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new RuntimeException("Mobile number already registered");
        }

        try {
            Owner owner = new Owner();
            owner.setFullName(request.getFullName());
            if(request.getEmail() != null)
                owner.setEmail(request.getEmail().toLowerCase());
            owner.setPassword(passwordEncoder.encode(request.getPassword()));
            owner.setMobileNumber(request.getMobileNumber());
            owner.setBusinessName(request.getBusinessName());

            try {
                if (request.getBusinessType() != null) {
                    owner.setBusinessType(Owner.BusinessType.valueOf(request.getBusinessType().toUpperCase()));
                }
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Invalid business type");
            }

            owner.setAddress(request.getAddress());
            owner.setSelectedPlan(Owner.Plan.valueOf("BASIC"));

            if (request.getPaymentMethod() != null) {
                owner.setPaymentMethod(Owner.PaymentMethod.valueOf(
                        request.getPaymentMethod().toUpperCase().replace(" ", "_")));
            }

            owner.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Asia/Kolkata");
//            owner.setProfilePictureUrl(request.getProfilePictureUrl());

            // Set trial and membership
            LocalDate today = LocalDate.now();
            owner.setTrialEndDate(today.plusDays(30));
//            owner.setMembershipEndDate(today.plusYears(1));
            owner.setAccountStatus(Owner.AccountStatus.TRIAL);
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                try {
                    byte[] compressed = ImageCompressor.compressImage(profilePhoto.getBytes());
                    owner.setProfilePhoto(compressed);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to compress profile photo", e);
                }
            }
            Owner savedOwner = ownerRepository.save(owner);

            // Optionally set JWT token in Owner object (if you have a field or want to return separately)
            // String token = jwtUtil.generateToken(savedOwner.getEmail(), "GYM_OWNER", savedOwner.getId());
            if (savedOwner.getEmail() != null) {
                String subject = "Welcome to GymNotify – Your 30 Trial Has Started\n";
                String htmlBody = buildWelcomeEmail(savedOwner.getFullName(), savedOwner.getTrialEndDate());
                emailService.sendHTMLEmail(savedOwner.getEmail(), subject, htmlBody);
            }

            return savedOwner;

        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public UserAuthResult authenticateUser(LoginRequest request) {
        String identifier = request.getIdentifier().trim().toLowerCase();
        String password = request.getPassword();

        // 1. Admin login (hardcoded)
        if (identifier.equals("admin@gym.com") && password.equals("admin123")) {
            return new UserAuthResult("admin@gym.com", "ADMIN", 0L, null); // 0 or dummy id
        }

        // 2. Owner login (from DB)
        Optional<Owner> ownerOpt = ownerRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            if (!passwordEncoder.matches(password, owner.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            return new UserAuthResult(owner.getEmail(), "OWNER", owner.getId(), owner);
        }

        // 3. Staff login (from DB)
//        Optional<Staff> staffOpt = staffRepository.findByEmailOrMobileNumber(identifier, identifier);
//        if (staffOpt.isPresent()) {
//            Staff staff = staffOpt.get();
//            if (!passwordEncoder.matches(password, staff.getPassword())) {
//                throw new RuntimeException("Invalid password");
//            }
//            return new UserAuthResult(staff.getEmail(), "STAFF", staff.getGymOwnerId());
//        }

        throw new RuntimeException("User not found");
    }

    @Override
    public void processForgotPassword(String mobileNumber) {
        Owner owner = ownerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User with given mobile number not found"));

        if (owner.getEmail() == null || owner.getEmail().isBlank()) {
            throw new RuntimeException("Registered email not found for this account.");
        }

        String token = refreshTokenService.generateResetToken(owner);
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        String subject = "Password Reset Request";
        String body = "Hi " + owner.getFullName() + ",\n\n"
                + "Click the link below to reset your password:\n"
                + resetLink + "\n\n"
                + "This link will expire in 30 minutes.";

        emailService.sendEmail(owner.getEmail(), subject, body);
    }

    private String buildWelcomeEmail(String name, LocalDate trialEndDate) {
        return """
    <html>
    <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">
        <div style="max-width: 600px; margin: auto; background-color: white; border-radius: 8px; padding: 30px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
            <h2 style="color: #2c3e50;">Hello %s,</h2>
            <p style="font-size: 16px; color: #34495e;">
                Welcome to <strong>GymNotify</strong>, your gym's all-in-one management tool.
            </p>
            <p style="font-size: 16px; color: #34495e;">
                Your complimentary 30-day trial is now active.
            </p>
            <div style="background-color: #ecf0f1; padding: 15px; border-left: 5px solid #2ecc71; margin: 20px 0;">
                <p style="font-size: 16px; margin: 0;">
                    Trial End Date: <strong style="color: #27ae60;">%s</strong>
                </p>
            </div>
            <p style="font-size: 16px; color: #34495e;">
                You can now start adding members, managing attendance, and tracking payments with ease.
            </p>
            <a href="https://www.gymnotify.com/dashboard" style="display: inline-block; padding: 12px 20px; background-color: #27ae60; color: white; text-decoration: none; border-radius: 6px; font-weight: bold;">
                Access Your Dashboard
            </a>
            <p style="font-size: 14px; color: #95a5a6; margin-top: 30px;">
                Need assistance? Simply reply to this message – we're happy to support you.
            </p>
            <p style="font-size: 14px; color: #bdc3c7;">– The GymNotify Team</p>
        </div>
    </body>
    </html>
    """.formatted(name, trialEndDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
    }

}
