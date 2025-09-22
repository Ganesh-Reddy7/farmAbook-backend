package com.farmabook.farmAbook.controller;

import com.farmabook.farmAbook.dto.InvestmentDTO;
import com.farmabook.farmAbook.dto.InvestmentWorkerDTO;
import com.farmabook.farmAbook.service.InvestmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping
    public ResponseEntity<InvestmentDTO> createInvestment(@Valid @RequestBody InvestmentDTO investmentDTO) {
        InvestmentDTO created = investmentService.createInvestment(investmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<InvestmentDTO>> getAllInvestments() {
        return ResponseEntity.ok(investmentService.getAllInvestments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentDTO> getInvestmentById(@PathVariable Long id) {
        return ResponseEntity.ok(investmentService.getInvestmentById(id));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<InvestmentDTO>> getInvestmentsByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(investmentService.getInvestmentsByFarmer(farmerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable Long id) {
        investmentService.deleteInvestment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-with-workers")
    public ResponseEntity<InvestmentDTO> createInvestmentWithWorkers(
            @Valid @RequestBody InvestmentWorkerDTO dto) {
        InvestmentDTO created = investmentService.createInvestmentWithWorkers(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/getInvestments/{year}")
    public ResponseEntity<List<InvestmentDTO>> getInvestmentsByFinancialYear(@PathVariable int year) {
        return ResponseEntity.ok(investmentService.getInvestmentsByFinancialYear(year));
    }

}
