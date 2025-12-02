package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.tractor.dto.TractorExpenseDTO;
import com.farmabook.farmAbook.tractor.dto.ExpenseTrendRangeDTO;
import com.farmabook.farmAbook.tractor.dto.YearlyExpenseDTO;
import com.farmabook.farmAbook.tractor.dto.MonthlyExpenseDTO;
import com.farmabook.farmAbook.tractor.dto.TractorExpenseSummaryDTO;
import com.farmabook.farmAbook.tractor.entity.Tractor;
import com.farmabook.farmAbook.tractor.entity.TractorExpense;
import com.farmabook.farmAbook.tractor.repository.TractorExpenseRepository;
import com.farmabook.farmAbook.tractor.repository.TractorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.Month;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TractorExpenseService {

    private final TractorExpenseRepository expenseRepository;
    private final TractorRepository tractorRepository;
    private final FarmerRepository farmerRepository;

    public TractorExpenseService(
            TractorExpenseRepository expenseRepository,
            TractorRepository tractorRepository,
            FarmerRepository farmerRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.tractorRepository = tractorRepository;
        this.farmerRepository = farmerRepository;
    }

    // ✅ Save expense from DTO
    public TractorExpenseDTO saveExpense(TractorExpenseDTO dto) {
        Tractor tractor = tractorRepository.findById(dto.getTractorId())
                .orElseThrow(() -> new EntityNotFoundException("Tractor not found with id " + dto.getTractorId()));

        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new EntityNotFoundException("Farmer not found with id " + dto.getFarmerId()));

        TractorExpense expense = new TractorExpense();
        expense.setTractor(tractor);
        expense.setFarmer(farmer);
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setType(dto.getType());
        expense.setLitres(dto.getLitres());
        expense.setCost(dto.getCost());
        expense.setNotes(dto.getNotes());

        TractorExpense saved = expenseRepository.save(expense);
        return mapToDTO(saved);
    }

    // ✅ Get expenses by farmer
    public List<TractorExpenseDTO> getExpensesByFarmer(Long farmerId) {
        return expenseRepository.findByFarmerId(farmerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get expenses by tractor
    public List<TractorExpenseDTO> getExpensesByTractor(Long tractorId) {
        return expenseRepository.findByTractorId(tractorId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ExpenseTrendRangeDTO getExpenseTrendRange(Long farmerId, int startYear, int endYear) {

        List<YearlyExpenseDTO> yearlyList = new ArrayList<>();

        for (int year = startYear; year <= endYear; year++) {

            List<TractorExpense> expenses =
                    expenseRepository.findByFarmerAndYear(farmerId, year);

            // Create monthly totals map initialized with zeros
            Map<Integer, Double> monthly = new HashMap<>();
            for (int m = 1; m <= 12; m++) monthly.put(m, 0.0);

            // Fill actual expenses
            for (TractorExpense e : expenses) {
                int month = e.getExpenseDate().getMonthValue();
                monthly.put(month, monthly.get(month) + (e.getCost() != null ? e.getCost() : 0.0));
            }

            // Convert to DTO list
            List<MonthlyExpenseDTO> monthlyDTOs = monthly.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> new MonthlyExpenseDTO(
                            Month.of(entry.getKey()).name().substring(0, 3),
                            entry.getValue()
                    ))
                    .collect(Collectors.toList());

            double yearlyTotal = expenses.stream()
                    .mapToDouble(e -> e.getCost() != null ? e.getCost() : 0.0)
                    .sum();

            YearlyExpenseDTO y = new YearlyExpenseDTO();
            y.setYear(year);
            y.setMonthlyExpenses(monthlyDTOs);
            y.setTotalYearExpense(yearlyTotal);

            yearlyList.add(y);
        }

        ExpenseTrendRangeDTO result = new ExpenseTrendRangeDTO();
        result.setFarmerId(farmerId);
        result.setStartYear(startYear);
        result.setEndYear(endYear);
        result.setYearlyData(yearlyList);

        return result;
    }

    public TractorExpenseSummaryDTO getFarmerExpenseSummary(Long farmerId) {

        List<TractorExpense> expenses = expenseRepository.findByFarmer(farmerId);

        double fuel = 0, repair = 0, other = 0;

        for (TractorExpense e : expenses) {
            switch (e.getType().toUpperCase()) {
                case "FUEL":
                    fuel += e.getCost();
                    break;

                case "REPAIR":
                    repair += e.getCost();
                    break;

                case "MAINTAINANCE":
                case "MAINTENANCE":
                case "OTHER":
                    other += e.getCost();
                    break;
            }
        }

        TractorExpenseSummaryDTO dto = new TractorExpenseSummaryDTO();
        dto.setFuelExpense(fuel);
        dto.setRepairExpense(repair);
        dto.setOtherExpense(other);
        dto.setTotalExpense(fuel + repair + other);

        return dto;
    }

    public List<TractorExpenseDTO> getExpensesByFilter(Long farmerId, Integer year, Integer month) {

        List<TractorExpense> expenses = expenseRepository.findExpensesFlexible(farmerId, year, month);

        return expenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Map Entity → DTO
    private TractorExpenseDTO mapToDTO(TractorExpense expense) {
        TractorExpenseDTO dto = new TractorExpenseDTO();
        dto.setId(expense.getId());
        dto.setTractorId(expense.getTractor().getId());
        dto.setFarmerId(expense.getFarmer().getId());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setType(expense.getType());
        dto.setLitres(expense.getLitres());
        dto.setCost(expense.getCost());
        dto.setNotes(expense.getNotes());
        return dto;
    }
}
