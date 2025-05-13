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
            owner.setTrialEndDate(today.plusDays(100));
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

}
