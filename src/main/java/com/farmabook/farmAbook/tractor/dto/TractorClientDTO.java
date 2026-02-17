package com.farmabook.farmAbook.tractor.dto;

import lombok.Data;

@Data
public class TractorClientDTO {
    // Getters & Setters
    private Long id;
    private Long farmerId;
    private String name;
    private String phone;
    private String address;
    private String notes;
    private String clientType;

    private Double totalAmount;        // Total amount earned from activities
    private Double amountReceived;     // Total paid by client
    private Double pendingAmount;      // Remaining
    private Double totalAcresWorked;   // Acres worked
    private Integer totalTrips;

}
