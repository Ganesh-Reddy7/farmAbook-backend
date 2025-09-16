package com.farmabook.farmAbook.controller;

import com.farmabook.farmAbook.dto.FarmerDTO;
import com.farmabook.farmAbook.service.FarmerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @PostMapping
    public ResponseEntity<FarmerDTO> createFarmer(@Valid @RequestBody FarmerDTO farmerDTO) {
        FarmerDTO created = farmerService.createFarmer(farmerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<FarmerDTO>> getAllFarmers() {
        return ResponseEntity.ok(farmerService.getAllFarmers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmerDTO> getFarmerById(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
        return ResponseEntity.noContent().build();
    }
}
