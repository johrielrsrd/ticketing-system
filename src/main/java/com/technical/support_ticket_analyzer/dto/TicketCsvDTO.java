package com.technical.support_ticket_analyzer.dto;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.*;

public class TicketCsvDTO {

    @NotNull (message = "ticketId must not be null")
    @CsvBindByName(column = "Ticket ID")
    private Long ticketId;

    @CsvBindByName(column = "Ticket subject")
    private String subject;

    @CsvBindByName(column = "Category")
    private String description;

    @CsvBindByName(column = "Ticket status")
    private String status;

    @CsvBindByName(column = "Ticket priority")
    private String priority;

    // Optional: createdAt can come from CSV, or you can auto-generate later
    @CsvBindByName(column = "Ticket created - Date")
    private String createdAt;

    // getters and setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
}
