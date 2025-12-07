package com.technical.support_ticket_analyzer.auth;

import com.technical.support_ticket_analyzer.auth.dto.RegisterUserDTO;
import com.technical.support_ticket_analyzer.users.model.User;
import com.technical.support_ticket_analyzer.users.model.Credential;
import com.technical.support_ticket_analyzer.users.UserRepository;
import com.technical.support_ticket_analyzer.users.CredentialRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository, CredentialRepository credentialRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.authenticationManager = authenticationManager;
    }

    // --- Registration ---
    public User registerUser(RegisterUserDTO newUser) {
        // Prevent duplicates
        String username = newUser.getUsername();

        if (credentialRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("Email is already in use.");
        }

        //get rawPass & hash it.
        String rawPassword = newUser.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Pass the attributes from DTO to User
        User user = new User(newUser.getFirstName(), newUser.getLastName(), newUser.getEmail());
        // Create and link credential
        Credential credential = new Credential(username, hashedPassword);
        user.setCredential(credential);

        // Save both user and credential
        return userRepository.save(user);
    }

    // --- Login ---
    public Credential login(String username, String password, HttpServletRequest httpRequest) {
        // Authenticate manually
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (!auth.isAuthenticated()) {
            throw new RuntimeException("Invalid credentials");
        }

        // Store authentication in security context
        SecurityContextHolder.getContext().setAuthentication(auth);

        // ✅ Create a session and bind the security context to it
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        // Successfully authenticated
        return credentialRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}