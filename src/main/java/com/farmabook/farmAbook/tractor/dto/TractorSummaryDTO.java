package com.farmabook.farmAbook.tractor.dto;

public class TractorSummaryDTO {
    private Long tractorId;
    private String serialNumber;
    private String model;
    private String make;
    private Integer capacityHp;
    private String status;

    private Double totalExpenses;
    private Double totalReturns;
    private Double netProfit;
    private Double totalFuelCost;
    private Double totalFuelLitres;


    private Double totalAreaWorked; // ✅ total acres worked
    private Integer totalTrips;     // ✅ number of activities

    // --- Getters & Setters ---
    public Long getTractorId() { return tractorId; }
    public void setTractorId(Long tractorId) { this.tractorId = tractorId; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public Integer getCapacityHp() { return capacityHp; }
    public void setCapacityHp(Integer capacityHp) { this.capacityHp = capacityHp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(Double totalExpenses) { this.totalExpenses = totalExpenses; }

    public Double getTotalReturns() { return totalReturns; }
    public void setTotalReturns(Double totalReturns) { this.totalReturns = totalReturns; }

    public Double getNetProfit() { return netProfit; }
    public void setNetProfit(Double netProfit) { this.netProfit = netProfit; }

    public Double getTotalAreaWorked() { return totalAreaWorked; }
    public void setTotalAreaWorked(Double totalAreaWorked) { this.totalAreaWorked = totalAreaWorked; }

    public Integer getTotalTrips() { return totalTrips; }
    public void setTotalTrips(Integer totalTrips) { this.totalTrips = totalTrips; }

    public Double getTotalFuelCost() {
        return totalFuelCost;
    }

    public void setTotalFuelCost(Double totalFuelCost) {
        this.totalFuelCost = totalFuelCost;
    }

    public Double getTotalFuelLitres() {
        return totalFuelLitres;
    }

    public void setTotalFuelLitres(Double totalFuelLitres) {
        this.totalFuelLitres = totalFuelLitres;
    }
}
