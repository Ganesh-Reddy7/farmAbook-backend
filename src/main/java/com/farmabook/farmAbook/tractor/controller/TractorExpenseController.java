package com.farmabook.farmAbook.tractor.controller;

import com.farmabook.farmAbook.tractor.entity.TractorExpense;
import com.farmabook.farmAbook.tractor.service.TractorExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tractor-expense")
public class TractorExpenseController {

    private final TractorExpenseService expenseService;

    public TractorExpenseController(TractorExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<TractorExpense> addExpense(@RequestBody TractorExpense expense) {
        TractorExpense saved = expenseService.saveExpense(expense);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<TractorExpense>> getExpensesByFarmer(@PathVariable Long farmerId) {
        List<TractorExpense> expenses = expenseService.getExpensesByFarmer(farmerId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/tractor/{tractorId}")
    public ResponseEntity<List<TractorExpense>> getExpensesByTractor(@PathVariable Long tractorId) {
        List<TractorExpense> expenses = expenseService.getExpensesByTractor(tractorId);
        return ResponseEntity.ok(expenses);
    }
}
