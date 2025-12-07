package com.technical.support_ticket_analyzer.tickets;

import com.technical.support_ticket_analyzer.tickets.model.Ticket;
import com.technical.support_ticket_analyzer.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);

    Optional<Ticket> findByTicketIdAndUserId(Long ticketId, Long userId);

    long countByUser(User user);

    long countByUserAndStatusIn(User user, List<String> statuses);
}
