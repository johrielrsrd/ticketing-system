package com.technical.support_ticket_analyzer.service;

import com.technical.support_ticket_analyzer.model.Ticket;
import com.technical.support_ticket_analyzer.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public List<Ticket> getAllTickets() {
        System.out.println("2️⃣ Service: Fetching all tickets from DB");
        List<Ticket> tickets = repository.findAll();
        System.out.println("4️⃣ Service: Found " + tickets.size() + " tickets");
        return tickets;
    }

    public Optional<Ticket> getTicketById(Long id) {
        return repository.findById(id);
    }

    public Ticket createTicket(Ticket ticket) {
         return repository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        return repository.findById(id)
                .map(ticket -> {
                    ticket.setSubject(updatedTicket.getSubject());
                    ticket.setDescription(updatedTicket.getDescription());
                    ticket.setStatus(updatedTicket.getDescription());
                    ticket.setPriority(updatedTicket.getPriority());
                    return repository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found."));
    }

    public void deleteTicket(Long id) {
        repository.deleteById(id);
    }
}
