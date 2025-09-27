package com.farmabook.farmAbook.loan.dto;

public class LoanPaymentDTO {

    private Double amount;
    private Long loanId;

    // Getters & Setters
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
}
