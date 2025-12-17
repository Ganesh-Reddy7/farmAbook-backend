package com.farmabook.farmAbook.tractor.dto;

import com.farmabook.farmAbook.tractor.dto.YearlyStatsDTO;
import lombok.Data;
import java.util.List;


@Data
public class YearlyStatsResponse {
    private Long farmerId;
    private int startYear;
    private int endYear;
    private List<YearlyStatsDTO> yearlyData;
}
