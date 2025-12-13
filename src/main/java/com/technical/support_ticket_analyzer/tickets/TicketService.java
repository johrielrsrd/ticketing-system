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

    public List<Ticket> allTicket() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsUploadedByUser(Long userId) {
        return ticketRepository.findByUploadedById(userId);
    }

    public Ticket createTicketForUser(Ticket ticket, String username) {
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        User user = credential.getUser();
        ticket.setUploadedBy(user);  // link user to ticket

        return ticketRepository.save(ticket);
    }

}
