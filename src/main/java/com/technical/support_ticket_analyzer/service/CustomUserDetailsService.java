package com.technical.support_ticket_analyzer.service;

import com.technical.support_ticket_analyzer.model.Credential;
import com.technical.support_ticket_analyzer.repository.CredentialRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final CredentialRepository credentialRepository;

    public CustomUserDetailsService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(credential.getUsername())
                .password(credential.getPasswordHash())
                .build();
    }
}
