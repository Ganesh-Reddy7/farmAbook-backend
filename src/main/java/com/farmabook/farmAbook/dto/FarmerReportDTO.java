package com.farmabook.farmAbook.dto;

public class FarmerReportDTO {
    private Long farmerId;
    private double totalInvestment;
    private double totalReturns;
    private double profitOrLoss;
    private double profitPercentage;
    private String periodLabel;

    public FarmerReportDTO(Long farmerId, double totalInvestment, double totalReturns,
                           double profitOrLoss, double profitPercentage, String periodLabel) {
        this.farmerId = farmerId;
        this.totalInvestment = totalInvestment;
        this.totalReturns = totalReturns;
        this.profitOrLoss = profitOrLoss;
        this.profitPercentage = profitPercentage;
        this.periodLabel = periodLabel;
    }

    // Getters
    public Long getFarmerId() {
        return farmerId;
    }

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public double getTotalReturns() {
        return totalReturns;
    }

    public double getProfitOrLoss() {
        return profitOrLoss;
    }

    public double getProfitPercentage() {
        return profitPercentage;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }
}
