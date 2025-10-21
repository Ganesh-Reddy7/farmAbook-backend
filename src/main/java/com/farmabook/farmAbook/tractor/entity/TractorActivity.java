package com.farmabook.farmAbook.tractor.entity;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.tractor.enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tractor_activity")
public class TractorActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tractor_id")
    private Tractor tractor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "acres_worked")
    private Double acresWorked;

    @Column(name = "amount_earned")
    private Double amountEarned = 0.0;

    @Column(name = "amount_paid")
    private Double amountPaid = 0.0;

    @Column(name = "amount_remaining")
    private Double amountRemaining = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public TractorActivity() {}

    /**
     * Ensures amountRemaining and paymentStatus are always correct
     * before persisting or updating.
     */
    @PrePersist
    @PreUpdate
    private void updatePaymentStatus() {
        recalculateAmounts();
    }

    /** Public helper to force recalculation (useful in service layer). */
    public void recalculateAmounts() {
        if (amountEarned == null) amountEarned = 0.0;
        if (amountPaid == null) amountPaid = 0.0;

        this.amountRemaining = Math.max(amountEarned - amountPaid, 0.0);

        if (amountPaid <= 0) {
            this.paymentStatus = PaymentStatus.PENDING;
        } else if (amountPaid < amountEarned) {
            this.paymentStatus = PaymentStatus.PARTIALLY_PAID;
        } else {
            this.paymentStatus = PaymentStatus.PAID;
        }
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tractor getTractor() { return tractor; }
    public void setTractor(Tractor tractor) { this.tractor = tractor; }

    public Farmer getFarmer() { return farmer; }
    public void setFarmer(Farmer farmer) { this.farmer = farmer; }

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

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
