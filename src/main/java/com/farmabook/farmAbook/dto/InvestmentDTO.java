package com.farmabook.farmAbook.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import lombok.Data;
import java.time.LocalDate;

@Data
public class InvestmentDTO {
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Date is required")
    private String date; // or LocalDate if you prefer

    @NotNull(message = "Farmer ID is required")
    private Long farmerId;  // relation to Farmer

    private Double remainingAmount; // ✅ new field


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public Double getRemainingAmount() { return remainingAmount; }

    public void setRemainingAmount(Double remainingAmount) { this.remainingAmount = remainingAmount; }
}
