package com.technical.support_ticket_analyzer.tickets;

import com.technical.support_ticket_analyzer.users.model.Credential;
import com.technical.support_ticket_analyzer.tickets.model.Ticket;
import com.technical.support_ticket_analyzer.users.model.User;
import com.technical.support_ticket_analyzer.users.CredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final CredentialRepository credentialRepository;

    public TicketService(TicketRepository ticketRepository, CredentialRepository credentialRepository) {
        this.ticketRepository = ticketRepository;
        this.credentialRepository = credentialRepository;
    }

    public List<Ticket> getTicketByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public Ticket createTicketForUser(Ticket ticket, String username) {
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        User user = credential.getUser();
        ticket.setUser(user);  // link user to ticket

        return ticketRepository.save(ticket);
    }

}
