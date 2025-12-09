package com.technical.support_ticket_analyzer.auth;

import com.technical.support_ticket_analyzer.auth.CustomUserDetail;
import com.technical.support_ticket_analyzer.users.model.Credential;
import com.technical.support_ticket_analyzer.users.CredentialRepository;
import com.technical.support_ticket_analyzer.users.model.User;
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
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        User user = credential.getUser();

        return new CustomUserDetail(user.getId(), credential.getUsername(), credential.getPasswordHash(), user.getFirstName(), user.getLastName(), user.getEmail(), java.util.List.of());
    }
}
