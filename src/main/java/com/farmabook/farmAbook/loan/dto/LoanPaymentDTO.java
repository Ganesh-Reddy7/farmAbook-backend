package com.farmabook.farmAbook.loan.dto;

import java.time.LocalDate;

public class LoanPaymentDTO {
    private Long id;
    private Double amount;
    private Double principalPaid;
    private Double interestPaid;
    private LocalDate paymentDate;
    private Long loanId;

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }


    // Getters and Setters
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
}
