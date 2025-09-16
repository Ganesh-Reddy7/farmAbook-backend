package com.farmabook.farmAbook.controller;

import com.farmabook.farmAbook.dto.ReturnEntryDTO;
import com.farmabook.farmAbook.service.ReturnEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
public class ReturnEntryController {

    private final ReturnEntryService returnEntryService;

    public ReturnEntryController(ReturnEntryService returnEntryService) {
        this.returnEntryService = returnEntryService;
    }

    @PostMapping
    public ResponseEntity<ReturnEntryDTO> createReturn(@Valid @RequestBody ReturnEntryDTO returnEntryDTO) {
        ReturnEntryDTO created = returnEntryService.createReturn(returnEntryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ReturnEntryDTO>> getAllReturns() {
        return ResponseEntity.ok(returnEntryService.getAllReturns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnEntryDTO> getReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(returnEntryService.getReturnById(id));
    }

    @GetMapping("/investment/{investmentId}")
    public ResponseEntity<List<ReturnEntryDTO>> getReturnsByInvestment(@PathVariable Long investmentId) {
        return ResponseEntity.ok(returnEntryService.getReturnsByInvestment(investmentId));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<ReturnEntryDTO>> getReturnsByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(returnEntryService.getReturnsByFarmerId(farmerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturn(@PathVariable Long id) {
        returnEntryService.deleteReturn(id);
        return ResponseEntity.noContent().build();
    }

    // Create return for a farmer (investment auto-detected)
    @PostMapping("/farmer/{farmerId}")
    public ResponseEntity<ReturnEntryDTO> createReturnForFarmer(
            @PathVariable Long farmerId,
            @Valid @RequestBody ReturnEntryDTO returnEntryDTO) {
        ReturnEntryDTO created = returnEntryService.createReturnForFarmer(farmerId, returnEntryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
