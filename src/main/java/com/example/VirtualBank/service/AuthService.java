package com.example.VirtualBank.service;

import com.example.VirtualBank.model.dto.LoginRequest;
import com.example.VirtualBank.model.dto.LoginResponse;
import com.example.VirtualBank.model.entity.User;
import com.example.VirtualBank.repository.UserRepository;
import com.example.VirtualBank.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    /**
     * Authenticate user and generate JWT token
     */
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for username: {}", loginRequest.getUsername());
        
        // Find user by username
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for username: {}", loginRequest.getUsername());
            throw new RuntimeException("Invalid username or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getEmail());
        
        log.info("Login successful for user: {}", user.getUsername());
        
        return new LoginResponse(token, user.getUserId(), user.getUsername(), user.getEmail());
    }
    
    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
