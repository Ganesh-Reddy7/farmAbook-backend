package com.farmabook.farmAbook.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CropReturnsDTO {
    private Long cropId;
    private String cropName;
    private Double totalReturns;
    private Double totalProduction;


    public CropReturnsDTO(Long cropId, String cropName, Double totalReturns, Double totalProduction) {
        this.cropId = cropId;
        this.cropName = cropName;
        this.totalReturns = totalReturns;
        this.totalProduction = totalProduction;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public Double getTotalReturns() {
        return totalReturns;
    }

    public void setTotalReturns(Double totalReturns) {
        this.totalReturns = totalReturns;
    }

    public Long getCropId() {
        return cropId;
    }

    public void setCropId(Long cropId) {
        this.cropId = cropId;
    }

    public Double getTotalProduction() {
        return totalProduction;
    }

    public void setTotalProduction(Double totalProduction) {
        this.totalProduction = totalProduction;
    }
// getters and setters
}
