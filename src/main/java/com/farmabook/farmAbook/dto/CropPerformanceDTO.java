package com.farmabook.farmAbook.dto;

public class CropPerformanceDTO {
    private Long cropId;
    private String cropName;
    private Double totalInvestment;
    private Double totalReturns;
    private Double profit;     // returns - investment
    private Double yield;      // total quantity (kg, tons)

    public CropPerformanceDTO() {}

    public CropPerformanceDTO(Long cropId, String cropName,
                              Double totalInvestment, Double totalReturns,
                              Double profit, Double yield) {
        this.cropId = cropId;
        this.cropName = cropName;
        this.totalInvestment = totalInvestment;
        this.totalReturns = totalReturns;
        this.profit = profit;
        this.yield = yield;
    }

    // getters and setters
    public Long getCropId() { return cropId; }
    public void setCropId(Long cropId) { this.cropId = cropId; }
    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }
    public Double getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(Double totalInvestment) { this.totalInvestment = totalInvestment; }
    public Double getTotalReturns() { return totalReturns; }
    public void setTotalReturns(Double totalReturns) { this.totalReturns = totalReturns; }
    public Double getProfit() { return profit; }
    public void setProfit(Double profit) { this.profit = profit; }
    public Double getYield() { return yield; }
    public void setYield(Double yield) { this.yield = yield; }
}
