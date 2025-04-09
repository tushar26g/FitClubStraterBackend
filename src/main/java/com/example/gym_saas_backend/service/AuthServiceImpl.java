package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.LoginRequest;
import com.example.gym_saas_backend.dto.RegisterRequest;
import com.example.gym_saas_backend.dto.UserAuthResult;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.entity.Staff;
import com.example.gym_saas_backend.repository.OwnerRepository;
import com.example.gym_saas_backend.repository.StaffRepository;
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
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public Owner registerOwner(RegisterRequest request) {
        if (ownerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (ownerRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new RuntimeException("Mobile number already registered");
        }

        try {
            Owner owner = new Owner();
            owner.setFullName(request.getFullName());
            owner.setEmail(request.getEmail());
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
            owner.setSelectedPlan(Owner.Plan.valueOf(request.getSelectedPlan().toUpperCase()));

            if (request.getPaymentMethod() != null) {
                owner.setPaymentMethod(Owner.PaymentMethod.valueOf(
                        request.getPaymentMethod().toUpperCase().replace(" ", "_")));
            }

            owner.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Asia/Kolkata");
            owner.setProfilePictureUrl(request.getProfilePictureUrl());

            // Set trial and membership
            LocalDate today = LocalDate.now();
            owner.setTrialEndDate(today.plusDays(90));
            owner.setMembershipEndDate(today.plusYears(1));
            owner.setAccountStatus(Owner.AccountStatus.TRIAL);

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
        String identifier = request.getIdentifier().trim();
        String password = request.getPassword();

        // 1. Admin login (hardcoded)
        if (identifier.equals("admin@gym.com") && password.equals("admin123")) {
            return new UserAuthResult("admin@gym.com", "ADMIN", 0L); // 0 or dummy id
        }

        // 2. Owner login (from DB)
        Optional<Owner> ownerOpt = ownerRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            if (!passwordEncoder.matches(password, owner.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            return new UserAuthResult(owner.getEmail(), "OWNER", owner.getId());
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

}
