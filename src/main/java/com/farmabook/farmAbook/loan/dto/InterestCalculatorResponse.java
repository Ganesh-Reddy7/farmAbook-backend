package com.farmabook.farmAbook.loan.dto;

import lombok.Data;

@Data
public class InterestCalculatorResponse {
    private Double interest;
    private Double totalAmount;
    private String type;
}
