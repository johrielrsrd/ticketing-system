package com.technical.support_ticket_analyzer.tickets;

import com.technical.support_ticket_analyzer.tickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);

    Optional<Ticket> findByTicketIdAndUserId(Long ticketId, Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndStatusIn(Long userId, List<String> statuses);
}
