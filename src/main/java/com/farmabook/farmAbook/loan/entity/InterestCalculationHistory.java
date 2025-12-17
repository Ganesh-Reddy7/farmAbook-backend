package com.farmabook.farmAbook.loan.entity;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "interest_calculation_history")
public class InterestCalculationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String calculationType;
    private Double principal;
    private Double rate;
    private Integer timeInMonths;

    private LocalDate startDate;     // ✅ REQUIRED
    private LocalDate endDate;       // ✅ REQUIRED
    private LocalDate calculationDate;
    // For compound
    private Integer compoundingFrequency; // 12, 4, 1 etc

    private Double interestAmount;
    private Double totalAmount;

    private Long farmerId;

}

