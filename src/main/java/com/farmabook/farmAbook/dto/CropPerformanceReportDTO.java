package com.farmabook.farmAbook.dto;

import java.util.List;

public class CropPerformanceReportDTO {
    private List<CropPerformanceDTO> topCrops;      // sorted by profit
    private List<CropPerformanceDTO> lowCrops;      // lowest profit crops
    private List<CropPerformanceDTO> cropDistribution; // for pie chart

    public CropPerformanceReportDTO() {}

    public CropPerformanceReportDTO(List<CropPerformanceDTO> topCrops,
                                    List<CropPerformanceDTO> lowCrops,
                                    List<CropPerformanceDTO> cropDistribution) {
        this.topCrops = topCrops;
        this.lowCrops = lowCrops;
        this.cropDistribution = cropDistribution;
    }

    // getters & setters
    public List<CropPerformanceDTO> getTopCrops() { return topCrops; }
    public void setTopCrops(List<CropPerformanceDTO> topCrops) { this.topCrops = topCrops; }
    public List<CropPerformanceDTO> getLowCrops() { return lowCrops; }
    public void setLowCrops(List<CropPerformanceDTO> lowCrops) { this.lowCrops = lowCrops; }
    public List<CropPerformanceDTO> getCropDistribution() { return cropDistribution; }
    public void setCropDistribution(List<CropPerformanceDTO> cropDistribution) { this.cropDistribution = cropDistribution; }
}
