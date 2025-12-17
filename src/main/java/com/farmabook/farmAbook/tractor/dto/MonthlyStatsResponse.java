package com.farmabook.farmAbook.tractor.dto;

import lombok.Data;
import java.util.List;

@Data
public class MonthlyStatsResponse {
    private Long farmerId;
    private Long tractorId;
    private int year;
    private List<MonthlyStatsDTO> monthlyData;
}
