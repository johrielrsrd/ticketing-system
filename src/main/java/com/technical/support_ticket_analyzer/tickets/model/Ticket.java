package com.technical.support_ticket_analyzer.tickets.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.technical.support_ticket_analyzer.users.model.User;
import jakarta.persistence.*;

import java.time.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ticketId;
    private String priority;
    private String status;
    private String subject;
    private String description;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "uploaded_by_user_id")
    @JsonBackReference
    private User uploadedBy;

    public Ticket() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
        if (uploadedBy != null && !uploadedBy.getTickets().contains(this)) {
            uploadedBy.getTickets().add(this);
        }
    }


}
