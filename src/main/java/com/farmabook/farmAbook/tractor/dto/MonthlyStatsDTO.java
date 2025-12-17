package com.farmabook.farmAbook.tractor.dto;

import lombok.Data;

@Data
public class MonthlyStatsDTO {
    private String month;
    private double returnsAmount;
    private double expenseAmount;
    private double fuelLitres;
    private double acresWorked;
}
