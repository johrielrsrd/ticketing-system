package com.technical.support_ticket_analyzer.tickets;

import com.technical.support_ticket_analyzer.tickets.model.Ticket;
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
    private final TicketService ticketService;
    private final CsvImportService csvImportService;

    public TicketController(TicketService ticketService, CsvImportService csvImportService) {
        this.ticketService = ticketService;
        this.csvImportService = csvImportService;
    }

    @GetMapping("/my-tickets")
    public List<Ticket> getMyTickets(Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Fetching tickets for user: " + username);
        return ticketService.getTicketsByUsername(username);
    }

    @PostMapping("create-new-ticket")
    public Ticket createTicket(@RequestBody Ticket ticket, Authentication authentication) {
        // Get the username of the logged-in user
        String username = authentication.getName();
        System.out.println("Creating tickets for user: " + username);
        // Pass username + ticket to service
        return ticketService.createTicketForUser(ticket, username);
    }

    @PostMapping("/upload-csv")
    public String uploadCsv(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        String username = authentication.getName();
        System.out.println("Uploading ticket for user: " + username);
        return csvImportService.importCsvFile(file, username);
    }
}
