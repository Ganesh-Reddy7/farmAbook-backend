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
}
