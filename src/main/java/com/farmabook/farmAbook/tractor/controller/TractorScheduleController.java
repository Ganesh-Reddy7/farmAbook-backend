package com.farmabook.farmAbook.tractor.controller;

import com.farmabook.farmAbook.tractor.enums.TractorScheduleStatus;
import com.farmabook.farmAbook.tractor.service.TractorScheduleService;
import com.farmabook.farmAbook.tractor.dto.*;
import com.farmabook.farmAbook.util.ApiResponseUtil;
import com.farmabook.farmAbook.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tractor/schedule")
@RequiredArgsConstructor
public class TractorScheduleController {

    private final TractorScheduleService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(
            @RequestBody CreateScheduleRequest request
    ) {
        try {
            service.createSchedule(request);
            return ApiResponseUtil.success(
                    "Tractor schedule created successfully",
                    null
            );

        } catch (Exception e) {
            return ApiResponseUtil.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create tractor schedule"
            );
        }
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<ApiResponse<List<TractorScheduleResponse>>> getByFarmer(
            @PathVariable Long farmerId
    ) {
        List<TractorScheduleResponse> schedules = service.getSchedulesByFarmer(farmerId);

        return ApiResponseUtil.success(
                "Schedules fetched successfully",
                schedules
        );
    }

    @GetMapping("/farmer/status/{farmerId}")
    public ResponseEntity<ApiResponse<List<TractorScheduleResponse>>> getByFarmer(
            @PathVariable Long farmerId,
            @RequestParam(required = false) TractorScheduleStatus status
    ) {
        List<TractorScheduleResponse> data = service.getSchedulesByFarmer(farmerId, status);

        return ApiResponseUtil.success(
                "Schedules fetched successfully",
                data
        );
    }

    @PatchMapping("/{scheduleId}/updateStatus")
    public ResponseEntity<ApiResponse<TractorScheduleResponse>> updateStatus(
            @PathVariable Long scheduleId,
            @RequestParam TractorScheduleStatus status
    ) {
        TractorScheduleResponse updated = service.updateScheduleStatus(scheduleId, status);
        return ApiResponseUtil.success("Status updated successfully", updated);
    }
}
