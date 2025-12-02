package com.farmabook.farmAbook.tractor.controller;

import com.farmabook.farmAbook.tractor.dto.TractorActivityDTO;
import com.farmabook.farmAbook.tractor.dto.ClientActivitySummaryDTO;
import com.farmabook.farmAbook.tractor.entity.TractorActivity;
import com.farmabook.farmAbook.tractor.service.TractorActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.farmabook.farmAbook.tractor.dto.ActivityTrendRangeResponse;

import java.util.List;

@RestController
@RequestMapping("/api/tractor-activities")
public class TractorActivityController {

    private final TractorActivityService activityService;

    public TractorActivityController(TractorActivityService activityService) {
        this.activityService = activityService;
    }

    // 1️⃣ Create a new activity (farmerId is included in DTO)
    @PostMapping("/create")
    public ResponseEntity<TractorActivityDTO> createActivity(@RequestBody TractorActivityDTO dto) {
        TractorActivity activity = activityService.createActivity(dto);
        return ResponseEntity.ok(activityService.toDTO(activity));
    }

    // 2️⃣ Add (partial or full) payment to existing activity
    @PostMapping("/add-payment")
    public ResponseEntity<TractorActivityDTO> addPayment(
            @RequestParam Long activityId,
            @RequestParam Double paymentAmount
    ) {
        TractorActivity updated = activityService.addPayment(activityId, paymentAmount);
        return ResponseEntity.ok(activityService.toDTO(updated));
    }

    // 3️⃣ Get activities for a given tractor
    @GetMapping("/tractor/{tractorId}")
    public ResponseEntity<List<TractorActivityDTO>> getByTractor(@PathVariable Long tractorId) {
        List<TractorActivityDTO> activities = activityService.getActivitiesByTractor(tractorId);
        return ResponseEntity.ok(activities);
    }

    // 4️⃣ Get activities for a given farmer
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<TractorActivityDTO>> getByFarmer(@PathVariable Long farmerId) {
        List<TractorActivityDTO> activities = activityService.getActivitiesByFarmer(farmerId);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/trend/range/{farmerId}")
    public ResponseEntity<ActivityTrendRangeResponse> getRangeTrend(
            @PathVariable Long farmerId,
            @RequestParam int startYear,
            @RequestParam int endYear) {

        return ResponseEntity.ok(
                activityService.getActivityTrendRange(farmerId, startYear, endYear)
        );
    }

    @GetMapping("/farmer/year-month-wise/{farmerId}/activities")
    public ResponseEntity<ClientActivitySummaryDTO> getActivitiesByFarmer(
            @PathVariable Long farmerId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        ClientActivitySummaryDTO summary = activityService.getActivitiesByFarmer(farmerId, year, month);
        return ResponseEntity.ok(summary);
    }

    // 5️⃣ Delete activity by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
