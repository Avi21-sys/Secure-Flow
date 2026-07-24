package com.secureflow.secureflow_backend.auth.service;

import com.secureflow.secureflow_backend.auth.dto.AuthResponse;
import com.secureflow.secureflow_backend.auth.dto.LoginRequest;
import com.secureflow.secureflow_backend.auth.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);
    AuthResponse login (LoginRequest request);
}
