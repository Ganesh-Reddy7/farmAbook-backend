package com.farmabook.farmAbook.tractor.dto;

import com.farmabook.farmAbook.tractor.dto.MonthlyExpenseDTO;

import java.util.List;

public class YearlyExpenseDTO {
    private int year;
    private List<MonthlyExpenseDTO> monthlyExpenses;
    private double totalYearExpense;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<MonthlyExpenseDTO> getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(List<MonthlyExpenseDTO> monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public double getTotalYearExpense() {
        return totalYearExpense;
    }

    public void setTotalYearExpense(double totalYearExpense) {
        this.totalYearExpense = totalYearExpense;
    }
}
