package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.tractor.dto.TractorExpenseDTO;
import com.farmabook.farmAbook.tractor.entity.Tractor;
import com.farmabook.farmAbook.tractor.entity.TractorExpense;
import com.farmabook.farmAbook.tractor.repository.TractorExpenseRepository;
import com.farmabook.farmAbook.tractor.repository.TractorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
