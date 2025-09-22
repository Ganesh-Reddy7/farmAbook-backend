package com.farmabook.farmAbook.dto;

public class YearlyInvestmentSummaryDTO {
    private int year;          // starting year of financial year
    private double totalAmount;

    public YearlyInvestmentSummaryDTO(int year, double totalAmount) {
        this.year = year;
        this.totalAmount = totalAmount;
    }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
