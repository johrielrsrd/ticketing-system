package com.technical.support_ticket_analyzer.dto;
import com.opencsv.bean.CsvBindByName;

public class TicketCsvDTO {
    @CsvBindByName(column = "subject")
    private String subject;

    @CsvBindByName(column = "description")
    private String description;

    @CsvBindByName(column = "status")
    private String status;

    @CsvBindByName(column = "priority")
    private String priority;

    // Optional: createdAt can come from CSV, or you can auto-generate later
    @CsvBindByName(column = "created_at")
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
}
