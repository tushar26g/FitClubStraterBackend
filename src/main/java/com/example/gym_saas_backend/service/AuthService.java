package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.AuthResponse;
import com.example.gym_saas_backend.dto.LoginRequest;
import com.example.gym_saas_backend.dto.RegisterRequest;
import com.example.gym_saas_backend.dto.UserAuthResult;
import com.example.gym_saas_backend.entity.Owner;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
//    AuthResponse register(RegisterRequest request);
//    AuthResponse login(LoginRequest request);
    Owner registerOwner(RegisterRequest request, MultipartFile profilePhoto);
    UserAuthResult authenticateUser(LoginRequest request);

}
