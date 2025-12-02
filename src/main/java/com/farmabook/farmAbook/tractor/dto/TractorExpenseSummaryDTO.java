package com.farmabook.farmAbook.tractor.dto;

public class TractorExpenseSummaryDTO {
    private double fuelExpense;
    private double repairExpense;
    private double otherExpense;
    private double totalExpense;

    public double getFuelExpense() {
        return fuelExpense;
    }

    public void setFuelExpense(double fuelExpense) {
        this.fuelExpense = fuelExpense;
    }

    public double getRepairExpense() {
        return repairExpense;
    }

    public void setRepairExpense(double repairExpense) {
        this.repairExpense = repairExpense;
    }

    public double getOtherExpense() {
        return otherExpense;
    }

    public void setOtherExpense(double otherExpense) {
        this.otherExpense = otherExpense;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }
}
