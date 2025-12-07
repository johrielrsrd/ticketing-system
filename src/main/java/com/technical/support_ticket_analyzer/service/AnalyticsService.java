package com.technical.support_ticket_analyzer.service;

import com.technical.support_ticket_analyzer.dto.SolveRateAnalyticsDTO;
import com.technical.support_ticket_analyzer.model.Credential;
import com.technical.support_ticket_analyzer.model.User;
import com.technical.support_ticket_analyzer.repository.CredentialRepository;
import com.technical.support_ticket_analyzer.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {
    private final CredentialRepository credentialRepository;
    private final TicketRepository ticketRepository;

    public AnalyticsService (CredentialRepository credentialRepository, TicketRepository ticketRepository) {
        this.credentialRepository = credentialRepository;
        this.ticketRepository = ticketRepository;
    }

    //Analytics: Solve Rate
    public SolveRateAnalyticsDTO getSolveRate(String username){
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user = credential.getUser();

        long totalCount = ticketRepository.countByUser(user);
        if (totalCount == 0) {
            SolveRateAnalyticsDTO result = new SolveRateAnalyticsDTO();
            result.setSolvedCount(0);
            result.setUnsolvedCount(0);
            result.setTotalCount(0);
            result.setSolveRatePercentage(0);
            return result;
        }

        long solvedCount = ticketRepository.countByUserAndStatusIn(user, List.of("Closed", "Solved"));
        long unsolvedCount = totalCount - solvedCount;
        double solveRate = ((double) solvedCount / totalCount) * 100;

        SolveRateAnalyticsDTO result = new SolveRateAnalyticsDTO();
        result.setSolvedCount(solvedCount);
        result.setSolveRatePercentage(solveRate);
        result.setTotalCount(totalCount);
        result.setUnsolvedCount(unsolvedCount);

        return result;
    }
}
