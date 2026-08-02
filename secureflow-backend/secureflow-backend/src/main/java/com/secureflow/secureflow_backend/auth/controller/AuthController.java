package com.secureflow.secureflow_backend.auth.controller;

import com.secureflow.secureflow_backend.auth.dto.AuthResponse;
import com.secureflow.secureflow_backend.auth.dto.LoginRequest;
import com.secureflow.secureflow_backend.auth.dto.RegisterRequest;
import com.secureflow.secureflow_backend.auth.service.AuthService;
import com.secureflow.secureflow_backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody RegisterRequest request
    ){
        String message = authService.register(request);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message(message)
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody LoginRequest request
    ){
        AuthResponse authResponse = authService.login(request);
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(authResponse)
                .build();
        return ResponseEntity.ok(response);

    }
}

