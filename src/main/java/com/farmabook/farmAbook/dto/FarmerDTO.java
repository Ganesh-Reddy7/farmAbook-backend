package com.farmabook.farmAbook.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class FarmerDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Location is required")
    private String location;
}
