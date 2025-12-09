package com.technical.support_ticket_analyzer.analytics;

import com.technical.support_ticket_analyzer.analytics.dto.SolveRateAnalyticsDTO;
import com.technical.support_ticket_analyzer.auth.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/analytics")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService){
        this.analyticsService = analyticsService;
    }

    @GetMapping("/solve-rate")
    public SolveRateAnalyticsDTO getMySolveRate(Authentication authentication) {
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        Long userId = user.getUserId();
        return analyticsService.getSolveRate(userId);
    }
}
