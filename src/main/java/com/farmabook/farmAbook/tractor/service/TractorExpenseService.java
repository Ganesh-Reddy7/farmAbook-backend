package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.tractor.entity.TractorExpense;
import com.farmabook.farmAbook.tractor.repository.TractorExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TractorExpenseService {

    private final TractorExpenseRepository expenseRepository;

    public TractorExpenseService(TractorExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public TractorExpense saveExpense(TractorExpense expense) {
        return expenseRepository.save(expense);
    }

    public List<TractorExpense> getExpensesByFarmer(Long farmerId) {
        return expenseRepository.findByFarmerId(farmerId);
    }

    public List<TractorExpense> getExpensesByTractor(Long tractorId) {
        return expenseRepository.findByTractorId(tractorId);
    }
}
