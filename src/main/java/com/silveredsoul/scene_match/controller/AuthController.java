package com.silveredsoul.scene_match.controller;

import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.UserRepository;
import com.silveredsoul.scene_match.service.JwtService;
import com.silveredsoul.scene_match.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username and password must not be empty"));
        }
        
        if (userRepo.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username already exists"));
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username and password must not be empty"));
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        try {
            authManager.authenticate(auth);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed: " + e.getMessage()));
        }

        User realUser = userRepo.findByUsername(user.getUsername()).orElseThrow();
        String token = jwtService.generateToken(realUser);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            blacklistService.blacklist(token);
            return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid token"));
    }
}

class AuthResponse {
    public String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}

class ErrorResponse {
    public String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}

class MessageResponse {
    public String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
