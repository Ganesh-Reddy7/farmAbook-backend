package com.farmabook.farmAbook.tractor.dto;

import com.farmabook.farmAbook.tractor.enums.AreaUnit;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateScheduleRequest {

    @NotNull
    private Long tractorId;

    @NotNull
    private Long farmerId;

    @NotNull
    private Long clientId;

    private Long fieldId;

    @DecimalMin(value = "0.01", message = "Area must be greater than zero")
    @NotNull
    private BigDecimal area;

    @NotNull
    private AreaUnit areaUnit;

    @NotNull
    @DecimalMin(value = "1.0", message = "Rate per acre must be positive")
    private BigDecimal ratePerAcre;

    @NotNull
    private String jobType;

    @NotNull
    private LocalDateTime scheduleDateTime;
    private String clientDisplayName;

    private Integer priorityOrder;
    private String notes;
}
