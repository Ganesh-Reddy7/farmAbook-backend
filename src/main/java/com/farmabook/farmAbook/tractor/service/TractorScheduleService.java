package com.farmabook.farmAbook.tractor.service;

import java.util.List;
import com.farmabook.farmAbook.tractor.dto.*;
import com.farmabook.farmAbook.tractor.enums.TractorScheduleStatus;

public interface TractorScheduleService {

    void createSchedule(CreateScheduleRequest request);

    List<TractorScheduleResponse> getSchedulesByFarmer(Long farmerId);

    List<TractorScheduleResponse> getSchedulesByFarmer(Long farmerId, TractorScheduleStatus status);
    TractorScheduleResponse updateScheduleStatus(Long scheduleId, TractorScheduleStatus newStatus);


}
