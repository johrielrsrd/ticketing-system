package com.technical.support_ticket_analyzer.analytics;

import com.technical.support_ticket_analyzer.analytics.dto.SolveRateAnalyticsDTO;
import com.technical.support_ticket_analyzer.tickets.TicketRepository;
import com.technical.support_ticket_analyzer.users.CredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {
    private final CredentialRepository credentialRepository;
    private final TicketRepository ticketRepository;

    public AnalyticsService(CredentialRepository credentialRepository, TicketRepository ticketRepository) {
        this.credentialRepository = credentialRepository;
        this.ticketRepository = ticketRepository;
    }

    //Analytics: Solve Rate
    public SolveRateAnalyticsDTO getSolveRate(Long userId) {
        long totalCount = ticketRepository.countByUploadedById(userId);
        if (totalCount == 0) {
            SolveRateAnalyticsDTO result = new SolveRateAnalyticsDTO();
            result.setSolvedCount(0);
            result.setUnsolvedCount(0);
            result.setTotalCount(0);
            result.setSolveRatePercentage(0);
            return result;
        }

        long solvedCount = ticketRepository.countByUploadedByIdAndStatusIn(userId, List.of("Closed", "Solved"));
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
