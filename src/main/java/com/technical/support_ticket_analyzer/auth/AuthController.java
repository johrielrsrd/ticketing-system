package com.technical.support_ticket_analyzer.auth;

import com.technical.support_ticket_analyzer.auth.dto.LoginRequestDTO;
import com.technical.support_ticket_analyzer.auth.dto.RegisterUserDTO;
import com.technical.support_ticket_analyzer.users.model.Credential;
import com.technical.support_ticket_analyzer.users.model.User;
import com.technical.support_ticket_analyzer.common.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // --- Register new user ---
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterUserDTO newUser) {
        try {
            User savedUser = authService.registerUser(newUser);
            Map<String, Object> response = Map.of(
                    "user", savedUser,
                    "message", "User registered successfully!"
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorBody = Map.of(
                    "message", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        }
    }

    // --- Login user ---
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest httpServletRequest) {
        Credential loggedInUser = authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword(), httpServletRequest);

        return ResponseEntity.ok("Currently Logged In: " + loggedInUser.getUsername());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate(); // Destroy the session
        SecurityContextHolder.clearContext(); // Clear authentication info
        return ResponseEntity.ok("You have successfully logged out.");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        String username = SecurityUtils.getByUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Not authenticated"));
        }
        return ResponseEntity.ok(Map.of("username", username));
    }
}