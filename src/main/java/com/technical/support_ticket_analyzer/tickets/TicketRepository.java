package com.technical.support_ticket_analyzer.tickets;

import com.technical.support_ticket_analyzer.tickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUploadedById(Long uploadedById);

    Optional<Ticket> findByTicketIdAndUploadedById(Long ticketId, Long uploadedById);

    long countByUploadedById(Long uploadedById);

    long countByUploadedByIdAndStatusIn(Long uploadedById, List<String> statuses);
}
