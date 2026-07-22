package com.secureflow.secureflow_backend.service;

import com.secureflow.secureflow_backend.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);
}
