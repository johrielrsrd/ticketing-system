package com.technical.support_ticket_analyzer.service;

import com.technical.support_ticket_analyzer.model.Credential;
import com.technical.support_ticket_analyzer.model.Ticket;
import com.technical.support_ticket_analyzer.model.User;
import com.technical.support_ticket_analyzer.repository.CredentialRepository;
import com.technical.support_ticket_analyzer.repository.TicketRepository;
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
