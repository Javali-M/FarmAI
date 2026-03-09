package com.example.agent_backend.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.agent_backend.dto.AuthResponse;
import com.example.agent_backend.dto.LoginRequest;
import com.example.agent_backend.dto.SignupRequest;
import com.example.agent_backend.model.UserProfile;
import com.example.agent_backend.repository.UserProfileRepository;
import com.example.agent_backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserProfileRepository userProfileRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse signup(SignupRequest request) {

        if (userProfileRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .message("Email already exists")
                    .build();
        }

        String userId = UUID.randomUUID().toString();

        UserProfile user = UserProfile.builder()
                .userId(userId)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .location(request.getLocation())
                .createdAt(Instant.now().toString())
                .build();

        userProfileRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), request.getLatitude(), request.getLongitude());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .userId(userId)
                .message("Signup successful")
                .build();
    }

    public AuthResponse login(LoginRequest request) {

        return userProfileRepository.findByEmail(request.getEmail())
                .map(user -> {
                    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        return AuthResponse.builder()
                                .message("Invalid password")
                                .build();
                    }

                    String token = jwtUtil.generateToken(user.getEmail(), request.getLatitude(), request.getLongitude());
                  

                    return AuthResponse.builder()
                            .token(token)
                            .email(user.getEmail())
                            .fullName(user.getFullName())
                            .userId(user.getUserId())
                            .message("Login successful")
                            .build();
                })
                .orElse(AuthResponse.builder()
                        .message("User not found")
                        .build());
    }
}
