package com.farmabook.farmAbook.tractor.entity;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.tractor.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
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

    @ManyToOne
    @JoinColumn(name = "client_id")
    private TractorClient client;


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

}
