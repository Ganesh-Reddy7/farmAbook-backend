package com.farmabook.farmAbook.tractor.dto;

import java.util.List;
import java.util.Map;

public class ClientActivitySummaryDTO {

    private List<TractorActivityDTO> activities;

    private double totalEarned;
    private double totalAmountRemaining;
    private double totalAmountToBeReceived;
    private double totalAcres;

    // Status-wise grouping: PENDING, PARTIALLY_PAID, PAID
    private Map<String, List<TractorActivityDTO>> statusWise;

    // getters and setters

    public List<TractorActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<TractorActivityDTO> activities) {
        this.activities = activities;
    }

    public double getTotalEarned() {
        return totalEarned;
    }

    public void setTotalEarned(double totalEarned) {
        this.totalEarned = totalEarned;
    }

    public double getTotalAmountRemaining() {
        return totalAmountRemaining;
    }

    public void setTotalAmountRemaining(double totalAmountRemaining) {
        this.totalAmountRemaining = totalAmountRemaining;
    }

    public double getTotalAmountToBeReceived() {
        return totalAmountToBeReceived;
    }

    public void setTotalAmountToBeReceived(double totalAmountToBeReceived) {
        this.totalAmountToBeReceived = totalAmountToBeReceived;
    }

    public Map<String, List<TractorActivityDTO>> getStatusWise() {
        return statusWise;
    }

    public void setStatusWise(Map<String, List<TractorActivityDTO>> statusWise) {
        this.statusWise = statusWise;
    }

    public double getTotalAcres() {
        return totalAcres;
    }

    public void setTotalAcres(double totalAcres) {
        this.totalAcres = totalAcres;
    }
}
