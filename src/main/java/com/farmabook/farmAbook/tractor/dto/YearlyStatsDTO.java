package com.farmabook.farmAbook.tractor.dto;
import lombok.Data;

@Data
public class YearlyStatsDTO {
    private int year;
    private double totalExpenses;
    private double totalReturns;
    private double fuelLitres;
    private double acresWorked;
}
