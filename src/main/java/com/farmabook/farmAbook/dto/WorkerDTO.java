package com.farmabook.farmAbook.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class WorkerDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Role is required")
    private String role;


    @NotNull(message = "Daily wage is required")
    @Min(value = 1, message = "Daily wage must be positive")
    private Double wage;

    @NotNull(message = "Investment ID is required")
    private Long investmentId;

    private boolean paymentDone = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getWage() {
        return wage;
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }

    public Long getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(Long investmentId) {
        this.investmentId = investmentId;
    }

    public boolean isPaymentDone() { return paymentDone; }

    public void setPaymentDone(boolean paymentDone) { this.paymentDone = paymentDone; }
}
