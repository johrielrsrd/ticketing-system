package com.technical.support_ticket_analyzer.controller;

import com.technical.support_ticket_analyzer.dto.LoginRequestDTO;
import com.technical.support_ticket_analyzer.model.Credential;
import com.technical.support_ticket_analyzer.model.User;
import com.technical.support_ticket_analyzer.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        User saved = authService.registerUser(user);
        return ResponseEntity.ok(saved);
    }

    // --- Login user ---
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest httpServletRequest) {
        Credential loggedInUser = authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword(), httpServletRequest);

        return ResponseEntity.ok("Currently Logged In: " + loggedInUser.getUsername());
    }
}