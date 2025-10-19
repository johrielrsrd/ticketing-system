package com.technical.support_ticket_analyzer.controller;

import com.technical.support_ticket_analyzer.dto.LoginRequest;
import com.technical.support_ticket_analyzer.model.User;
import com.technical.support_ticket_analyzer.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // --- Register new user ---
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User saved = authService.registerUser(user, user.getCredential().getPasswordHash());
        return ResponseEntity.ok(saved);
    }

    // --- Login user ---
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        User loggedInUser = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(loggedInUser);
    }
}