package com.technical.support_ticket_analyzer.tickets;

import com.technical.support_ticket_analyzer.users.model.Credential;
import com.technical.support_ticket_analyzer.tickets.model.Ticket;
import com.technical.support_ticket_analyzer.users.model.User;
import com.technical.support_ticket_analyzer.users.CredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final CredentialRepository credentialRepository;

    public TicketService(TicketRepository ticketRepository, CredentialRepository credentialRepository) {
        this.ticketRepository = ticketRepository;
        this.credentialRepository = credentialRepository;
    }


    public List<Ticket> getTicketsByUsername(String username) {
        System.out.println("2️⃣ Service: Fetching tickets for username: " + username);
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        User user = credential.getUser();
        List<Ticket> tickets = ticketRepository.findByUser(user);
        System.out.println("4️⃣ Service: Found " + tickets.size() + " tickets for user " + username);
        return tickets;
    }

    public Ticket createTicketForUser(Ticket ticket, String username) {
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        User user = credential.getUser();
        ticket.setUser(user);  // link user to ticket

        return ticketRepository.save(ticket);
    }

}
