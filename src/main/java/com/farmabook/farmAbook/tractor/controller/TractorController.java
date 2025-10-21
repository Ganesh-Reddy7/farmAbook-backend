package com.farmabook.farmAbook.tractor.controller;

import com.farmabook.farmAbook.tractor.dto.TractorDTO;
import com.farmabook.farmAbook.tractor.dto.TractorResponseDTO;
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
    @PostMapping
    public ResponseEntity<TractorResponseDTO> addTractor(@RequestBody TractorDTO tractorDTO) {
        TractorResponseDTO saved = tractorService.saveTractor(tractorDTO);
        return ResponseEntity.ok(saved);
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

    // Delete tractor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTractor(@PathVariable Long id) {
        tractorService.deleteTractor(id);
        return ResponseEntity.noContent().build();
    }
}
