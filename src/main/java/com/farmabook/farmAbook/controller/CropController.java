package com.farmabook.farmAbook.controller;

import com.farmabook.farmAbook.dto.CropDTO;
import com.farmabook.farmAbook.service.CropService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<CropDTO> addCrop(@Valid @RequestBody CropDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cropService.addCrop(dto));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<CropDTO>> getCropsByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(cropService.getCropsByFarmer(farmerId));
    }

    @GetMapping("/farmer/{farmerId}/range")
    public ResponseEntity<List<CropDTO>> getCropsByDateRange(
            @PathVariable Long farmerId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return ResponseEntity.ok(cropService.getCropsByFarmerAndDateRange(farmerId, start, end));
    }

    @GetMapping("/farmer/{farmerId}/financial-year/{year}")
    public ResponseEntity<List<CropDTO>> getCropsByFinancialYear(
            @PathVariable Long farmerId,
            @PathVariable int year) {

        return ResponseEntity.ok(cropService.getCropsByFinancialYear(farmerId, year));
    }

    @GetMapping("/farmer/{farmerId}/dropdown/fy/{year}")
    public ResponseEntity<List<CropDTO>> getCropsDropdownByFY(
            @PathVariable Long farmerId,
            @PathVariable int year) {

        List<CropDTO> crops = cropService.getCropsDropdownByFinancialYear(farmerId, year);
        return ResponseEntity.ok(crops);
    }

}
