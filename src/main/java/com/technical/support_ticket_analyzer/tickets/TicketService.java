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

    public List<Ticket> getAllTickets() {
        System.out.println("2️⃣ Service: Fetching all tickets from DB");
        List<Ticket> tickets = ticketRepository.findAll();
        System.out.println("4️⃣ Service: Found " + tickets.size() + " tickets");
        return tickets;
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicketForUser(Ticket ticket, String username) {
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = credential.getUser();
        ticket.setUser(user);  // link user to ticket

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUsername(String username) {
        System.out.println("2️⃣ Service: Fetching tickets for username: " + username);
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user = credential.getUser();
        List<Ticket> tickets = ticketRepository.findByUser(user);
        System.out.println("4️⃣ Service: Found " + tickets.size() + " tickets for user " + username);
        return tickets;
    }


    //this service is outdated and is a known bug.
    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setSubject(updatedTicket.getSubject());
                    ticket.setDescription(updatedTicket.getDescription());
                    ticket.setStatus(updatedTicket.getDescription());
                    ticket.setPriority(updatedTicket.getPriority());
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found."));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
