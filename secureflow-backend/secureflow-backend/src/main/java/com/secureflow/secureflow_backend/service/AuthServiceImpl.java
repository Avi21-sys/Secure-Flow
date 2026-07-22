package com.secureflow.secureflow_backend.service;

import com.secureflow.secureflow_backend.dto.RegisterRequest;
import com.secureflow.secureflow_backend.entity.Role;
import com.secureflow.secureflow_backend.entity.User;
import com.secureflow.secureflow_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public String register(RegisterRequest request) {
        // Check if the email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Create a new user entity and set its properties
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                // Encode the password before saving it to the database
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.DEVELOPER) // Set the default role to DEVELOPER
                .build();

        userRepository.save(user); // Save the user to the database

        return "User registered successfully";
    }
}
