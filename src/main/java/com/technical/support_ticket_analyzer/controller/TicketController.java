package com.technical.support_ticket_analyzer.controller;

import com.technical.support_ticket_analyzer.model.Ticket;
import com.technical.support_ticket_analyzer.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*") // Allow all connections for now.
public class TicketController {
    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        System.out.println("1️⃣ Controller: Received request for all tickets");
        List<Ticket> tickets = service.getAllTickets();
        System.out.println("5️⃣ Controller: Returning response");
        return tickets;
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return service.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return service.createTicket(ticket);
    }

    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        return service.updateTicket(id, ticket);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        service.deleteTicket(id);
    }
}
