package com.technical.support_ticket_analyzer.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.technical.support_ticket_analyzer.dto.TicketCsvDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.List;

@Service
public class CsvImportService {

    public List<TicketCsvDTO> importCsvFile(MultipartFile file) {
        try {
            var reader = new InputStreamReader(file.getInputStream());
            return new CsvToBeanBuilder<TicketCsvDTO>(reader)
                    .withType(TicketCsvDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
