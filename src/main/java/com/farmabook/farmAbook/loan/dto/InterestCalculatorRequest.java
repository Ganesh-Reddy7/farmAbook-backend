package com.farmabook.farmAbook.loan.dto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InterestCalculatorRequest {
    private Double principal;
    private Double rate; // % per month or year (defined by type)
    private Integer timeInMonths;

    private LocalDate startDate;
    private LocalDate endDate;

    // COMPOUND only
    private Integer compoundingFrequency; // 12 = monthly

    private String type; // SIMPLE / COMPOUND

    private Long farmerId; // optional
}
