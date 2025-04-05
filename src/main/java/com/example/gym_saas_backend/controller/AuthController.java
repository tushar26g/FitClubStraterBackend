package com.example.gym_saas_backend.controller;
import com.example.gym_saas_backend.dto.AuthResponse;
import com.example.gym_saas_backend.dto.LoginRequest;
import com.example.gym_saas_backend.dto.RegisterRequest;
import com.example.gym_saas_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED)
                .body(response);
    }

}

