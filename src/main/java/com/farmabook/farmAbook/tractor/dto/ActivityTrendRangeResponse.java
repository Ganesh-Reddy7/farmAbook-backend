package com.farmabook.farmAbook.tractor.dto;
import  com.farmabook.farmAbook.tractor.dto.ActivityYearlyDataDTO;

import java.util.List;
import lombok.Data;

@Data
public class ActivityTrendRangeResponse {
    private Long farmerId;
    private int startYear;
    private int endYear;
    private List<ActivityYearlyDataDTO> yearlyData;

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public List<ActivityYearlyDataDTO> getYearlyData() {
        return yearlyData;
    }

    public void setYearlyData(List<ActivityYearlyDataDTO> yearlyData) {
        this.yearlyData = yearlyData;
    }
}
