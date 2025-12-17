package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.tractor.entity.Tractor;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.tractor.entity.TractorExpense;
import com.farmabook.farmAbook.tractor.entity.TractorActivity;
import com.farmabook.farmAbook.tractor.repository.TractorRepository;
import com.farmabook.farmAbook.tractor.repository.TractorExpenseRepository;
import com.farmabook.farmAbook.tractor.repository.TractorActivityRepository;
import com.farmabook.farmAbook.tractor.dto.TractorDTO;
import com.farmabook.farmAbook.tractor.dto.YearlyStatsResponse;
import com.farmabook.farmAbook.tractor.dto.YearlyStatsDTO;
import com.farmabook.farmAbook.tractor.dto.TractorResponseDTO;
import com.farmabook.farmAbook.tractor.dto.TractorSummaryDTO;
import com.farmabook.farmAbook.tractor.dto.*;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TractorService {

    private final TractorRepository tractorRepository;
    private final TractorExpenseRepository expenseRepo;
    private final TractorActivityRepository activityRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public TractorService(TractorRepository tractorRepository , TractorExpenseRepository expenseRepo,
                          TractorActivityRepository activityRepo) {
        this.tractorRepository = tractorRepository;
        this.expenseRepo = expenseRepo;
        this.activityRepo = activityRepo;
    }

    // Save tractor and return as response DTO
    public TractorResponseDTO saveTractor(TractorDTO dto) {
        Farmer farmer = entityManager.find(Farmer.class, dto.getFarmerId());
        if (farmer == null) {
            throw new RuntimeException("Farmer not found with id " + dto.getFarmerId());
        }

        Tractor tractor = new Tractor();
        tractor.setSerialNumber(dto.getSerialNumber());
        tractor.setModel(dto.getModel());
        tractor.setMake(dto.getMake());
        tractor.setCapacityHp(dto.getCapacityHp());
        tractor.setStatus(dto.getStatus());
        tractor.setFarmer(farmer);

        // 🆕 Set new fields
        tractor.setPurchaseDate(dto.getPurchaseDate());
        tractor.setPurchaseCost(dto.getPurchaseCost());

        Tractor saved = tractorRepository.save(tractor);
        return toDTO(saved);
    }

    public List<TractorSummaryDTO> getTractorSummariesByFarmer(Long farmerId) {
        List<Tractor> tractors = tractorRepository.findByFarmerId(farmerId);
        List<TractorSummaryDTO> summaries = new ArrayList<>();

        for (Tractor tractor : tractors) {
            double totalExpenses = tractor.getExpenses().stream()
                    .mapToDouble(e -> e.getCost() != null ? e.getCost() : 0.0)
                    .sum();

            double totalFuelCost = tractor.getExpenses().stream()
                    .filter(e -> e.getType() != null && e.getType().equalsIgnoreCase("FUEL"))
                    .mapToDouble(e -> e.getCost() != null ? e.getCost() : 0.0)
                    .sum();

            double totalFuelLitres = tractor.getExpenses().stream()
                    .filter(e -> e.getType() != null && e.getType().equalsIgnoreCase("FUEL"))
                    .mapToDouble(e -> e.getLitres() != null ? e.getLitres() : 0.0)
                    .sum();

            double totalReturns = tractor.getActivities().stream()
                    .mapToDouble(a -> a.getAmountEarned() != null ? a.getAmountEarned() : 0.0)
                    .sum();

            double totalAreaWorked = tractor.getActivities().stream()
                    .mapToDouble(a -> a.getAcresWorked() != null ? a.getAcresWorked() : 0.0)
                    .sum();

            int totalTrips = tractor.getActivities().size();

            TractorSummaryDTO dto = new TractorSummaryDTO();
            dto.setTractorId(tractor.getId());
            dto.setSerialNumber(tractor.getSerialNumber());
            dto.setModel(tractor.getModel());
            dto.setMake(tractor.getMake());
            dto.setCapacityHp(tractor.getCapacityHp());
            dto.setStatus(tractor.getStatus());
            dto.setTotalExpenses(totalExpenses);
            dto.setTotalFuelCost(totalFuelCost);
            dto.setTotalFuelLitres(totalFuelLitres);
            dto.setTotalReturns(totalReturns);
            dto.setNetProfit(totalReturns - totalExpenses);
            dto.setTotalAreaWorked(totalAreaWorked);
            dto.setTotalTrips(totalTrips);

            summaries.add(dto);
        }

        return summaries;
    }

    // Get all tractors by farmer and convert to response DTOs
    public List<TractorResponseDTO> getAllTractorsByFarmer(Long farmerId) {
        List<Tractor> tractors = tractorRepository.findByFarmerId(farmerId);
        return tractors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TractorResponseDTO> getTractorById(Long id) {
        return tractorRepository.findById(id).map(this::toDTO);
    }

    public void deleteTractor(Long id) {
        tractorRepository.deleteById(id);
    }

    public YearlyStatsResponse getYearlyStats(Long farmerId, int startYear, int endYear) {
        List<YearlyStatsDTO> yearlyData = new ArrayList<>();

        for (int year = startYear; year <= endYear; year++) {
            List<TractorExpense> expenses = expenseRepo.findByFarmerAndYear(farmerId, year);
            List<TractorActivity> activities = activityRepo.findByFarmerAndYear(farmerId, year);
            double totalExpenses = expenses.stream()
                    .mapToDouble(TractorExpense::getCost)
                    .sum();
            double fuelLitres = expenses.stream()
                    .filter(e -> e.getType().equalsIgnoreCase("FUEL"))
                    .mapToDouble(TractorExpense::getLitres)
                    .sum();
            double totalReturns = activities.stream()
                    .mapToDouble(TractorActivity::getAmountEarned)
                    .sum();
            double acresWorked = activities.stream()
                    .mapToDouble(TractorActivity::getAcresWorked)
                    .sum();

            YearlyStatsDTO dto = new YearlyStatsDTO();
            dto.setYear(year);
            dto.setTotalExpenses(totalExpenses);
            dto.setTotalReturns(totalReturns);
            dto.setFuelLitres(fuelLitres);
            dto.setAcresWorked(acresWorked);
            yearlyData.add(dto);
        }
        YearlyStatsResponse response = new YearlyStatsResponse();
        response.setFarmerId(farmerId);
        response.setStartYear(startYear);
        response.setEndYear(endYear);
        response.setYearlyData(yearlyData);
        return response;
    }

    public MonthlyStatsResponse getMonthlyStats(Long farmerId, Integer year, Long tractorId) {
        List<TractorActivity> activities = activityRepo.findMonthlyReturnsFlexible(farmerId, year, tractorId);

        List<TractorExpense> expenses = expenseRepo.findMonthlyExpensesFlexible(farmerId, year, tractorId);
        Map<Integer, MonthlyStatsDTO> map = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            MonthlyStatsDTO dto = new MonthlyStatsDTO();
            dto.setMonth(Month.of(i).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            map.put(i, dto);
        }
        for (TractorActivity a : activities) {
            int m = a.getActivityDate().getMonthValue();
            MonthlyStatsDTO dto = map.get(m);

            dto.setReturnsAmount(dto.getReturnsAmount() + a.getAmountEarned());
            dto.setAcresWorked(dto.getAcresWorked() + a.getAcresWorked());
        }
        for (TractorExpense e : expenses) {
            int m = e.getExpenseDate().getMonthValue();
            MonthlyStatsDTO dto = map.get(m);

            dto.setExpenseAmount(dto.getExpenseAmount() + e.getCost());

            if (e.getLitres() != null) {
                dto.setFuelLitres(dto.getFuelLitres() + e.getLitres());
            }
        }
        MonthlyStatsResponse response = new MonthlyStatsResponse();
        response.setFarmerId(farmerId);
        response.setTractorId(tractorId);
        response.setYear(year);
        response.setMonthlyData(new ArrayList<>(map.values()));
        return response;
    }

    private TractorResponseDTO toDTO(Tractor tractor) {
        TractorResponseDTO dto = new TractorResponseDTO();
        dto.setId(tractor.getId());
        dto.setSerialNumber(tractor.getSerialNumber());
        dto.setModel(tractor.getModel());
        dto.setMake(tractor.getMake());
        dto.setCapacityHp(tractor.getCapacityHp());
        dto.setStatus(tractor.getStatus());
        dto.setFarmerId(tractor.getFarmer().getId()); // only ID
        return dto;
    }
}
