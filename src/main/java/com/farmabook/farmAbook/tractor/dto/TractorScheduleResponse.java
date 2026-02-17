package com.farmabook.farmAbook.tractor.dto;

import com.farmabook.farmAbook.tractor.enums.AreaUnit;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.farmabook.farmAbook.tractor.enums.TractorScheduleStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TractorScheduleResponse {

    private Long id;
    private Long tractorId;
    private Long farmerId;
    private Long clientId;
    private String jobType;
    private LocalDateTime scheduleDateTime;
    private TractorScheduleStatus status;
    private Integer priorityOrder;
    private String clientDisplayName;
    private BigDecimal area;
    private AreaUnit areaUnit;
    private BigDecimal areaInAcres;
    private BigDecimal expectedJobCost;
    private BigDecimal expectedFuelLitres;
    private BigDecimal expectedFuelCost;

}
