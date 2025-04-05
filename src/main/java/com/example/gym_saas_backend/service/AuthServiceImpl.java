package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.AuthResponse;
import com.example.gym_saas_backend.dto.LoginRequest;
import com.example.gym_saas_backend.dto.RegisterRequest;
import com.example.gym_saas_backend.entity.Owner;
//import com.example.gym_saas_backend.model.User;
import com.example.gym_saas_backend.repository.OwnerRepository;
//import com.example.gym_saas_backend.repository.UserRepository;
import com.example.gym_saas_backend.service.AuthService;
import com.example.gym_saas_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (ownerRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email already registered", null);
        }

        if (ownerRepository.existsByMobileNumber(request.getMobileNumber())) {
            return new AuthResponse(false, "Mobile number already registered", null);
        }

        try {
            Owner owner = new Owner();
            owner.setFullName(request.getFullName());
            owner.setEmail(request.getEmail());
            owner.setPassword(passwordEncoder.encode(request.getPassword()));
            owner.setMobileNumber(request.getMobileNumber());
            owner.setBusinessName(request.getBusinessName());
            try {
                if(request.getBusinessType()!=null)
                    owner.setBusinessType(Owner.BusinessType.valueOf(request.getBusinessType().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                return new AuthResponse(false, "Invalid business type", null);
            }

            if(request.getAddress()!=null)
                owner.setAddress(request.getAddress());

            owner.setSelectedPlan(Owner.Plan.valueOf(request.getSelectedPlan().toUpperCase()));
            owner.setPaymentMethod(request.getPaymentMethod() != null ? Owner.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase().replace(" ", "_")) : null);
            owner.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Asia/Kolkata");
            if(request.getProfilePictureUrl()!=null)
                owner.setProfilePictureUrl(request.getProfilePictureUrl());

            LocalDate today = LocalDate.now();
            owner.setTrialEndDate(today.plusDays(90));
            owner.setMembershipEndDate(today.plusYears(1));
            owner.setAccountStatus(Owner.AccountStatus.TRIAL);

            Owner savedOwner = ownerRepository.save(owner);

            String token = jwtUtil.generateToken(savedOwner.getEmail(), "GYM_OWNER"); // Adjust if your JWT takes roles
            return new AuthResponse(true, "Owner registered successfully", token);

        } catch (Exception e) {
            return new AuthResponse(false, "Registration failed: " + e.getMessage(), null);
        }
    }


    @Override
    public AuthResponse login(LoginRequest request) {
        String identifier = request.getIdentifier().trim();
        Optional<Owner> ownerOpt = ownerRepository.findByEmailOrMobileNumber(identifier, identifier);

        if (ownerOpt.isEmpty()) {
            return new AuthResponse(false, "Invalid email or mobile number", null);
        }

        Owner owner = ownerOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
            return new AuthResponse(false, "Invalid password", null);
        }

        String token = jwtUtil.generateToken(owner.getEmail(), "GYM_OWNER");

        return new AuthResponse(true, "Login successful", token);
    }
}
