package com.farmabook.farmAbook.loan.controller;

import com.farmabook.farmAbook.loan.repository.LoanPaymentRepository;
import com.farmabook.farmAbook.loan.dto.LoanTransactionDTO;
import com.farmabook.farmAbook.loan.dto.*;
import com.farmabook.farmAbook.loan.entity.LoanPayment;
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
    private final LoanPaymentRepository loanPaymentRepository;

    public LoanController(LoanService loanService , LoanPaymentRepository loanPaymentRepository) {
        this.loanService = loanService;
        this.loanPaymentRepository = loanPaymentRepository;
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

    @GetMapping("/{loanId}/payments")
    public ResponseEntity<List<LoanPayment>> getPaymentsByLoan(@PathVariable Long loanId) {
        List<LoanPayment> payments = loanPaymentRepository.findByLoanId(loanId);
        return ResponseEntity.ok(payments);
    }

    //  GET /api/loans/farmer/{farmerId}/type?isGiven=true
    @GetMapping("/farmer/{farmerId}/type")
    public ResponseEntity<List<LoanTransactionDTO>> getLoansByFarmerAndType(
            @PathVariable Long farmerId,
            @RequestParam Boolean isGiven) {
        return ResponseEntity.ok(loanService.getLoansByFarmerAndType(farmerId, isGiven));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<LoanTransactionDTO> closeLoan(@PathVariable Long id) {
        LoanTransactionDTO closedLoan = loanService.closeLoan(id);
        return ResponseEntity.ok(closedLoan);
    }

    @GetMapping("/{loanId}/raw-payments")
    public ResponseEntity<List<LoanPaymentDTO>> getLoanPayments(@PathVariable Long loanId) {
        List<LoanPaymentDTO> payments = loanService.getPaymentsByLoanId(loanId);
        return ResponseEntity.ok(payments);
    }

}
