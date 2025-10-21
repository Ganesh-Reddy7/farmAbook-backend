package com.farmabook.farmAbook.tractor.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class TractorActivityDTO {
    private Long id;
    private Long tractorId;
    private Long farmerId;
    private LocalDate activityDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String clientName;
    private Double acresWorked;
    private Double amountEarned;
    private Double amountPaid;
    private Double amountRemaining;
    private String paymentStatus;
    private String notes;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTractorId() { return tractorId; }
    public void setTractorId(Long tractorId) { this.tractorId = tractorId; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public Double getAcresWorked() { return acresWorked; }
    public void setAcresWorked(Double acresWorked) { this.acresWorked = acresWorked; }

    public Double getAmountEarned() { return amountEarned; }
    public void setAmountEarned(Double amountEarned) { this.amountEarned = amountEarned; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public Double getAmountRemaining() { return amountRemaining; }
    public void setAmountRemaining(Double amountRemaining) { this.amountRemaining = amountRemaining; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
