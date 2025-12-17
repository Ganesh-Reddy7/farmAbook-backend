package com.farmabook.farmAbook.tractor.controller;

import com.farmabook.farmAbook.tractor.dto.TractorDTO;
import com.farmabook.farmAbook.tractor.dto.YearlyStatsResponse;
import com.farmabook.farmAbook.tractor.dto.TractorResponseDTO;
import com.farmabook.farmAbook.tractor.dto.TractorSummaryDTO;
import com.farmabook.farmAbook.tractor.dto.MonthlyStatsResponse;
import com.farmabook.farmAbook.tractor.service.TractorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tractor")
public class TractorController {

    private final TractorService tractorService;

    public TractorController(TractorService tractorService) {
        this.tractorService = tractorService;
    }

    // Add a tractor
    @PostMapping("/addTractor")
    public ResponseEntity<TractorResponseDTO> addTractor(@RequestBody TractorDTO tractorDTO) {
        TractorResponseDTO saved = tractorService.saveTractor(tractorDTO);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/farmer/{farmerId}/getTractorDetails")
    public ResponseEntity<List<TractorSummaryDTO>> getTractorSummary(@PathVariable Long farmerId) {
        List<TractorSummaryDTO> summaries = tractorService.getTractorSummariesByFarmer(farmerId);
        return ResponseEntity.ok(summaries);
    }

    // Get all tractors by farmer
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<TractorResponseDTO>> getTractorsByFarmer(@PathVariable Long farmerId) {
        List<TractorResponseDTO> tractors = tractorService.getAllTractorsByFarmer(farmerId);
        return ResponseEntity.ok(tractors);
    }

    // Get tractor by ID
    @GetMapping("/{id}")
    public ResponseEntity<TractorResponseDTO> getTractor(@PathVariable Long id) {
        return tractorService.getTractorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/yearly-stats")
    public ResponseEntity<YearlyStatsResponse> getYearlyStats(
            @RequestParam Long farmerId,
            @RequestParam int startYear,
            @RequestParam int endYear
    ) {
        return ResponseEntity.ok(tractorService.getYearlyStats(farmerId, startYear, endYear));
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyStatsResponse> getMonthlyStats(
            @RequestParam Long farmerId,
            @RequestParam Integer year,
            @RequestParam(required = false) Long tractorId
    ) {
        return ResponseEntity.ok(tractorService.getMonthlyStats(farmerId, year, tractorId));
    }

    // Delete tractor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTractor(@PathVariable Long id) {
        tractorService.deleteTractor(id);
        return ResponseEntity.noContent().build();
    }
}
