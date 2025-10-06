package com.farmabook.farmAbook.loan.entity;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
public class LoanPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount; // Total amount paid in this payment

    private Double principalPaid; // Portion of payment applied to principal
    private Double interestPaid;  // Portion of payment applied to interest

    private LocalDate paymentDate;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanTransaction loan;

    // ----------------- Getters and Setters -----------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Double getPrincipalPaid() { return principalPaid; }
    public void setPrincipalPaid(Double principalPaid) { this.principalPaid = principalPaid; }

    public Double getInterestPaid() { return interestPaid; }
    public void setInterestPaid(Double interestPaid) { this.interestPaid = interestPaid; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public LoanTransaction getLoan() { return loan; }
    public void setLoan(LoanTransaction loan) { this.loan = loan; }
}
