package com.farmabook.farmAbook.loan.controller;

import com.farmabook.farmAbook.loan.dto.SummaryDTO;
import com.farmabook.farmAbook.loan.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping("/{farmerId}")
    public ResponseEntity<SummaryDTO> getFarmerSummary(@PathVariable Long farmerId) {
        SummaryDTO summary = summaryService.getFarmerSummary(farmerId);
        return ResponseEntity.ok(summary);
    }
}
