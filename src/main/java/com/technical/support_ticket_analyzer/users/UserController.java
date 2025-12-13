package com.technical.support_ticket_analyzer.users;

import com.technical.support_ticket_analyzer.auth.CustomUserDetail;
import com.technical.support_ticket_analyzer.users.dto.LoggedUserDTO;
import com.technical.support_ticket_analyzer.users.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public LoggedUserDTO currentlyLoggedInUser (Authentication authentication) {
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return new LoggedUserDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

//Below here are just prototypes.
    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return service.getUserByEmail(email)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );
    }
}
