package com.technical.support_ticket_analyzer.users;

import com.technical.support_ticket_analyzer.users.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    // Find credential by user ID
    Optional<Credential> findByUsername(String username);
    boolean existsByUsername (String username);
    // Optional: find by password hash (rarely used directly)
    Optional<Credential> findByPasswordHash(String passwordHash);
}