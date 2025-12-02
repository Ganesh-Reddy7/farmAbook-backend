package com.farmabook.farmAbook.tractor.dto;

import java.util.List;
import lombok.Data;


public class ExpenseTrendRangeDTO {
    private Long farmerId;
    private int startYear;
    private int endYear;
    private List<YearlyExpenseDTO> yearlyData;

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

    public List<YearlyExpenseDTO> getYearlyData() {
        return yearlyData;
    }

    public void setYearlyData(List<YearlyExpenseDTO> yearlyData) {
        this.yearlyData = yearlyData;
    }
}
