package com.farmabook.farmAbook.tractor.controller;

import com.farmabook.farmAbook.tractor.dto.TractorExpenseDTO;
import com.farmabook.farmAbook.tractor.dto.ExpenseTrendRangeDTO;
import com.farmabook.farmAbook.tractor.dto.YearlyExpenseDTO;
import com.farmabook.farmAbook.tractor.dto.TractorExpenseSummaryDTO;
import com.farmabook.farmAbook.tractor.service.TractorExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tractor-expenses")
public class TractorExpenseController {

    private final TractorExpenseService expenseService;

    public TractorExpenseController(TractorExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // ✅ Create new expense
    @PostMapping
    public ResponseEntity<TractorExpenseDTO> addExpense(@RequestBody TractorExpenseDTO expense) {
        TractorExpenseDTO saved = expenseService.saveExpense(expense);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get all expenses for a farmer
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<TractorExpenseDTO>> getExpensesByFarmer(@PathVariable Long farmerId) {
        List<TractorExpenseDTO> expenses = expenseService.getExpensesByFarmer(farmerId);
        return ResponseEntity.ok(expenses);
    }

    // ✅ Get all expenses for a tractor
    @GetMapping("/tractor/{tractorId}")
    public ResponseEntity<List<TractorExpenseDTO>> getExpensesByTractor(@PathVariable Long tractorId) {
        List<TractorExpenseDTO> expenses = expenseService.getExpensesByTractor(tractorId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/expense-trend-range")
    public ResponseEntity<ExpenseTrendRangeDTO> getExpenseTrendRange(
            @RequestParam Long farmerId,
            @RequestParam int startYear,
            @RequestParam int endYear
    ) {
        return ResponseEntity.ok(
                expenseService.getExpenseTrendRange(farmerId, startYear, endYear)
        );
    }

    @GetMapping("/farmer/{farmerId}/summary")
    public ResponseEntity<TractorExpenseSummaryDTO> getSummary(@PathVariable Long farmerId) {
        return ResponseEntity.ok(expenseService.getFarmerExpenseSummary(farmerId));
    }

    @GetMapping("/farmer/year-month-wise/{farmerId}")
    public ResponseEntity<List<TractorExpenseDTO>> getExpenses(
            @PathVariable Long farmerId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        return ResponseEntity.ok(expenseService.getExpensesByFilter(farmerId, year, month));
    }


}
