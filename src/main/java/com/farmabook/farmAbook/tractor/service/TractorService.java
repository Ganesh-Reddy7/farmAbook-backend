package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.tractor.entity.Tractor;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.tractor.repository.TractorRepository;
import com.farmabook.farmAbook.tractor.dto.TractorDTO;
import com.farmabook.farmAbook.tractor.dto.TractorResponseDTO;
import com.farmabook.farmAbook.tractor.dto.TractorSummaryDTO;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TractorService {

    private final TractorRepository tractorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TractorService(TractorRepository tractorRepository) {
        this.tractorRepository = tractorRepository;
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

    // Convert entity to response DTO
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
