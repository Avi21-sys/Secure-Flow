package com.secureflow.secureflow_backend.service;

import com.secureflow.secureflow_backend.dto.AuthResponse;
import com.secureflow.secureflow_backend.dto.LoginRequest;
import com.secureflow.secureflow_backend.dto.RegisterRequest;
import com.secureflow.secureflow_backend.entity.Role;
import com.secureflow.secureflow_backend.entity.User;
import com.secureflow.secureflow_backend.repository.UserRepository;
import com.secureflow.secureflow_backend.security.CustomUserDetailsService;
import com.secureflow.secureflow_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

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

    @Override
    public AuthResponse login(LoginRequest request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                request.getEmail()
                        );


        String token =
                jwtService.generateToken(userDetails);


        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
