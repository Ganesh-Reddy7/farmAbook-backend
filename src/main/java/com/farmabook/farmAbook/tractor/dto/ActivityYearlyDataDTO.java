package com.farmabook.farmAbook.tractor.dto;
import  com.farmabook.farmAbook.tractor.dto.ActivityMonthlyDTO;

import java.util.List;
import lombok.Data;


@Data
public class ActivityYearlyDataDTO {
    private int year;
    private List<ActivityMonthlyDTO> monthlyActivities;
    private double totalYearAmount;
    private double totalYearReceived;
    private double totalYearRemaining;
    private double totalYearAcres;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ActivityMonthlyDTO> getMonthlyActivities() {
        return monthlyActivities;
    }

    public void setMonthlyActivities(List<ActivityMonthlyDTO> monthlyActivities) {
        this.monthlyActivities = monthlyActivities;
    }

    public double getTotalYearAmount() {
        return totalYearAmount;
    }

    public void setTotalYearAmount(double totalYearAmount) {
        this.totalYearAmount = totalYearAmount;
    }

    public double getTotalYearReceived() {
        return totalYearReceived;
    }

    public void setTotalYearReceived(double totalYearReceived) {
        this.totalYearReceived = totalYearReceived;
    }

    public double getTotalYearRemaining() {
        return totalYearRemaining;
    }

    public void setTotalYearRemaining(double totalYearRemaining) {
        this.totalYearRemaining = totalYearRemaining;
    }

    public double getTotalYearAcres() {
        return totalYearAcres;
    }

    public void setTotalYearAcres(double totalYearAcres) {
        this.totalYearAcres = totalYearAcres;
    }
}
