package com.farmabook.farmAbook.tractor.dto;

public class MonthlyExpenseDTO {

    private String month;
    private double total;

    public MonthlyExpenseDTO() {}

    public MonthlyExpenseDTO(String month, double total) {
        this.month = month;
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
