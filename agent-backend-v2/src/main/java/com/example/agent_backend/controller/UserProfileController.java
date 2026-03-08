package com.example.agent_backend.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.agent_backend.model.UserProfile;
import com.example.agent_backend.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/users")
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;

    @PostMapping
    public ResponseEntity<UserProfile> createUser(@RequestBody UserProfile userProfile) {
        if (userProfile.getUserId() == null || userProfile.getUserId().isEmpty()) {
            userProfile.setUserId(UUID.randomUUID().toString());
        }
        userProfile.setCreatedAt(Instant.now().toString());
        userProfileRepository.save(userProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUser(@PathVariable String userId) {
        return userProfileRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> getAllUsers() {
        return ResponseEntity.ok(userProfileRepository.findAll());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfile> updateUser(@PathVariable String userId, @RequestBody UserProfile userProfile) {
        return userProfileRepository.findById(userId)
                .map(existing -> {
                    userProfile.setUserId(userId);
                    userProfile.setCreatedAt(existing.getCreatedAt());
                    userProfileRepository.save(userProfile);
                    return ResponseEntity.ok(userProfile);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userProfileRepository.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
