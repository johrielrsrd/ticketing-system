package com.technical.support_ticket_analyzer.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.technical.support_ticket_analyzer.dto.TicketCsvDTO;
import com.technical.support_ticket_analyzer.model.Credential;
import com.technical.support_ticket_analyzer.model.Ticket;
import com.technical.support_ticket_analyzer.model.User;
import com.technical.support_ticket_analyzer.repository.CredentialRepository;
import com.technical.support_ticket_analyzer.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {
    private final CredentialRepository credentialRepository;
    private final TicketRepository ticketRepository;

    public CsvImportService(CredentialRepository credentialRepository, TicketRepository ticketRepository) {
        this.credentialRepository = credentialRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<TicketCsvDTO> importCsvFile(MultipartFile file, String username) {
        try {
            var reader = new InputStreamReader(file.getInputStream());
            List<TicketCsvDTO> uploadedCSV = new CsvToBeanBuilder<TicketCsvDTO>(reader)
                    .withType(TicketCsvDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            System.out.println("This is an uploaded file: " + uploadedCSV);
            saveToTicketModel(uploadedCSV, username);
            return uploadedCSV;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

// This is how you save the uploaded CSV to the ticketModel
    public void saveToTicketModel(List<TicketCsvDTO> uploadedCSV, String username) {
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = credential.getUser();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Ticket> ticketsToSave = new ArrayList<>();

        for (TicketCsvDTO csvItem : uploadedCSV) {
            Ticket itemToSave = new Ticket();
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
    }
}
