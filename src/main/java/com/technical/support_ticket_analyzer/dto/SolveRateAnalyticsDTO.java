package com.technical.support_ticket_analyzer.dto;

public class SolveRateAnalyticsDTO {
    double solveRatePercentage;
    long solvedCount;
    long unsolvedCount;
    long totalCount;

    public double getSolveRatePercentage() {
        return solveRatePercentage;
    }

    public void setSolveRatePercentage(double solveRatePercentage) {
        this.solveRatePercentage = solveRatePercentage;
    }

    public long getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(long solvedCount) {
        this.solvedCount = solvedCount;
    }

    public long getUnsolvedCount() {
        return unsolvedCount;
    }

    public void setUnsolvedCount(long unsolvedCount) {
        this.unsolvedCount = unsolvedCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
