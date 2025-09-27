package com.farmabook.farmAbook.loan.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class LoanPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private LocalDate paymentDate;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private LoanTransaction loan;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public LoanTransaction getLoan() { return loan; }
    public void setLoan(LoanTransaction loan) { this.loan = loan; }
}
