package com.technical.support_ticket_analyzer.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.technical.support_ticket_analyzer.dto.TicketCsvDTO;
import com.technical.support_ticket_analyzer.model.Credential;
import com.technical.support_ticket_analyzer.model.Ticket;
import com.technical.support_ticket_analyzer.model.User;
import com.technical.support_ticket_analyzer.repository.CredentialRepository;
import com.technical.support_ticket_analyzer.repository.TicketRepository;
import jakarta.validation.ConstraintViolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Validator;

@Service
public class CsvImportService {
    private final CredentialRepository credentialRepository;
    private final TicketRepository ticketRepository;
    private final Validator validator;

    public CsvImportService(CredentialRepository credentialRepository, TicketRepository ticketRepository, Validator validator) {
        this.credentialRepository = credentialRepository;
        this.ticketRepository = ticketRepository;
        this.validator = validator;
    }

    public String importCsvFile(MultipartFile file, String username) throws IOException {
            var reader = new InputStreamReader(file.getInputStream());
            List<TicketCsvDTO> uploadedCSV = new CsvToBeanBuilder<TicketCsvDTO>(reader)
                    .withType(TicketCsvDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            System.out.println("This is an uploaded file: " + uploadedCSV);

            for (int i = 0; i < uploadedCSV.size(); i++) {
                TicketCsvDTO row = uploadedCSV.get(i);
                int rowNumber = i + 1;

                Set<ConstraintViolation<TicketCsvDTO>> violations = validator.validate(row);
                if (!violations.isEmpty()) {
                    String message = violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "));
                    throw new IllegalArgumentException("Row " + rowNumber + ": " + message);
                }
            }

            return saveToTicketModel(uploadedCSV, username);
    }

    //saving uploadedCSV to Ticket model.
    @Transactional
    public String saveToTicketModel(List<TicketCsvDTO> uploadedCSV, String username) {
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = credential.getUser();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Ticket> ticketsToSave = new ArrayList<>();

        int insertedCount = 0;
        int updatedCount = 0;

        for (TicketCsvDTO csvItem : uploadedCSV) {
            var existingTicket = ticketRepository
                    .findByTicketIdAndUserId(csvItem.getTicketId(), user.getId());

            Ticket itemToSave;
            if (existingTicket.isPresent()) {
                itemToSave = existingTicket.get();
                updatedCount++;
            } else {
                itemToSave = new Ticket();
                insertedCount++;
            }

            itemToSave.setTicketId(csvItem.getTicketId());
            itemToSave.setSubject(csvItem.getSubject());
            itemToSave.setDescription(csvItem.getDescription());
            itemToSave.setStatus(csvItem.getStatus());
            itemToSave.setPriority(csvItem.getPriority());
            itemToSave.setCreatedAt(LocalDate.parse(csvItem.getCreatedAt(), formatter).atStartOfDay());

            itemToSave.setUser(user);

            ticketsToSave.add(itemToSave);
        }

        ticketRepository.saveAll(ticketsToSave);

        System.out.println("Saved " + ticketsToSave.size() + " tickets to DB");

        return "CSV import completed. Total rows: " + uploadedCSV.size()
                + ", Inserted: " + insertedCount
                + ", Updated: " + updatedCount;
    }
}
