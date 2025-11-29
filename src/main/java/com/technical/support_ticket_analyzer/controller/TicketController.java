package com.technical.support_ticket_analyzer.controller;

import com.technical.support_ticket_analyzer.dto.TicketCsvDTO;
import com.technical.support_ticket_analyzer.model.Ticket;
import com.technical.support_ticket_analyzer.service.CsvImportService;
import com.technical.support_ticket_analyzer.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class TicketController {
    private final TicketService service;
    private final CsvImportService csvImportService;

    public TicketController(TicketService service, CsvImportService csvImportService) {
        this.service = service;
        this.csvImportService = csvImportService;
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        System.out.println("1️⃣ Controller: Received request for all tickets");
        List<Ticket> tickets = service.getAllTickets();
        System.out.println("5️⃣ Controller: Returning response");
        return tickets;
    }

    @GetMapping("/my-tickets")
    public List<Ticket> getMyTickets(Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Fetching tickets for user: " + username);
        return service.getTicketsByUsername(username);
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return service.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket, Authentication authentication) {
        // Get the username of the logged-in user
        String username = authentication.getName();
        System.out.println("Creating tickets for user: " + username);
        // Pass username + ticket to service
        return service.createTicketForUser(ticket, username);
    }

    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        return service.updateTicket(id, ticket);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        service.deleteTicket(id);
    }

    @PostMapping("/upload-csv")
    public List<TicketCsvDTO> uploadCsv(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        String username = authentication.getName();
        System.out.println("Uploading ticket for user: " + username);
        return csvImportService.importCsvFile(file, username);
    }
}
