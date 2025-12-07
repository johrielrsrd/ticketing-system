package com.technical.support_ticket_analyzer.users;

import com.technical.support_ticket_analyzer.users.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        List<User> users = repository.findAll();
        return users;
    }

    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
}
