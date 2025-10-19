package com.technical.support_ticket_analyzer.repository;

import com.technical.support_ticket_analyzer.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
