package com.farmabook.farmAbook.loan.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InterestCalculationHistoryDTO {

    private Long id;
    private String calculationType; // SIMPLE / COMPOUND
    private Double principal;
    private Double rate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer timeInMonths;
    private Integer compoundingFrequency;
    private Double interestAmount;
    private Double totalAmount;
    private LocalDate calculationDate;
    private Long farmerId;
}
