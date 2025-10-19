package com.technical.support_ticket_analyzer.service;

import com.technical.support_ticket_analyzer.model.User;
import com.technical.support_ticket_analyzer.model.Credential;
import com.technical.support_ticket_analyzer.repository.UserRepository;
import com.technical.support_ticket_analyzer.repository.CredentialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, CredentialRepository credentialRepository) {
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // --- Registration ---
    public User registerUser(User user, String rawPassword) {
        // Prevent duplicates
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use.");
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Create and link credential
        Credential credential = new Credential(hashedPassword);
        user.setCredential(credential);

        // Save both user and credential
        return userRepository.save(user);
    }

    // --- Login ---
    public User login(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password.");
        }

        User user = userOpt.get();
        String storedHash = user.getCredential().getPasswordHash();

        if (!passwordEncoder.matches(rawPassword, storedHash)) {
            throw new RuntimeException("Invalid username or password.");
        }

        return user;
    }
}