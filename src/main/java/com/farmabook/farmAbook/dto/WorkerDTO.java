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
}
