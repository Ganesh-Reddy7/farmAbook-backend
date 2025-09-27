package com.farmabook.farmAbook.loan.controller;

import com.farmabook.farmAbook.loan.dto.LoanTransactionDTO;
import com.farmabook.farmAbook.loan.dto.LoanPaymentDTO;
import com.farmabook.farmAbook.loan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanTransactionDTO> createLoan(@RequestBody LoanTransactionDTO dto , @RequestParam(value = "bondImage", required = false) MultipartFile bondImage) throws IOException{
        LoanTransactionDTO created = loanService.createLoan(dto, bondImage);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/payment")
    public ResponseEntity<LoanTransactionDTO> makePayment(@RequestBody LoanPaymentDTO dto) {
        return ResponseEntity.ok(loanService.makePayment(dto));
    }

    @GetMapping("/current-interest/{loanId}")
    public ResponseEntity<Double> getCurrentInterest(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.calculateCurrentInterest(loanId));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<LoanTransactionDTO>> getLoansByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(loanService.getLoansByFarmer(farmerId));
    }
}
