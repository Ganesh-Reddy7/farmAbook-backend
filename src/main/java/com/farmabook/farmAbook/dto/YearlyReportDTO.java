package com.farmabook.farmAbook.dto;

public class YearlyReportDTO {
    private int year;                     // start year of financial year (May YEAR -> Apr YEAR+1)
    private Double totalInvestment;       // sum of investment.amount for FY
    private Double totalRemaining;        // sum of investment.remainingAmount for FY
    private Double totalReturns;          // sum of return_entry.amount for FY
    private Double totalProduction;       // sum of return_entry.quantity for FY

    public YearlyReportDTO() {}

    public YearlyReportDTO(int year, Double totalInvestment, Double totalRemaining,
                           Double totalReturns, Double totalProduction) {
        this.year = year;
        this.totalInvestment = totalInvestment;
        this.totalRemaining = totalRemaining;
        this.totalReturns = totalReturns;
        this.totalProduction = totalProduction;
    }

    // getters & setters
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public Double getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(Double totalInvestment) { this.totalInvestment = totalInvestment; }
    public Double getTotalRemaining() { return totalRemaining; }
    public void setTotalRemaining(Double totalRemaining) { this.totalRemaining = totalRemaining; }
    public Double getTotalReturns() { return totalReturns; }
    public void setTotalReturns(Double totalReturns) { this.totalReturns = totalReturns; }
    public Double getTotalProduction() { return totalProduction; }
    public void setTotalProduction(Double totalProduction) { this.totalProduction = totalProduction; }
}
