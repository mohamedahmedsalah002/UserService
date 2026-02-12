package com.example.VirtualBank.controller;

import com.example.VirtualBank.model.dto.LoginRequest;
import com.example.VirtualBank.model.dto.LoginResponse;
import com.example.VirtualBank.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Login endpoint
     * POST /auth/login
     * Body: { "username": "john", "password": "password123" }
     * Response: { "token": "jwt-token", "userId": "uuid", "username": "john", "email": "john@email.com" }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request received for username: {}", loginRequest.getUsername());
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Validate token endpoint (optional - for testing)
     * GET /auth/validate?token=your-jwt-token
     */
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        boolean isValid = authService.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(401).body("Token is invalid");
        }
    }
}
