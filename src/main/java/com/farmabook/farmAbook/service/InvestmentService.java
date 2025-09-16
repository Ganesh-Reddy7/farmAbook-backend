package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.InvestmentDTO;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.entity.Investment;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestmentService {
    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    public InvestmentDTO createInvestment(InvestmentDTO dto) {
        Investment investment = new Investment();
        investment.setAmount(dto.getAmount());
        investment.setDescription(dto.getDescription());

        // convert String -> LocalDate (if provided)
        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            try {
                investment.setDate(LocalDate.parse(dto.getDate())); // expects "yyyy-MM-dd"
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format for investment.date. Expected yyyy-MM-dd");
            }
        }

        // link farmer if provided
        if (dto.getFarmerId() != null) {
            Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id " + dto.getFarmerId()));
            investment.setFarmer(farmer);
        }

        Investment saved = investmentRepository.save(investment);

        dto.setId(saved.getId());
        return dto;
    }

    // Get all (GET)
    public List<InvestmentDTO> getAllInvestments() {
        return investmentRepository.findAll().stream().map(inv -> {
            InvestmentDTO d = new InvestmentDTO();
            d.setId(inv.getId());
            d.setAmount(inv.getAmount());
            d.setDescription(inv.getDescription());
            d.setDate(inv.getDate() != null ? inv.getDate().toString() : null); // LocalDate -> String
            d.setFarmerId(inv.getFarmer() != null ? inv.getFarmer().getId() : null);
            return d;
        }).collect(Collectors.toList());
    }

    // Get by ID (GET /{id})
    public InvestmentDTO getInvestmentById(Long id) {
        Investment inv = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found with id " + id));

        InvestmentDTO d = new InvestmentDTO();
        d.setId(inv.getId());
        d.setAmount(inv.getAmount());
        d.setDescription(inv.getDescription());
        d.setDate(inv.getDate() != null ? inv.getDate().toString() : null);
        d.setFarmerId(inv.getFarmer() != null ? inv.getFarmer().getId() : null);
        return d;
    }

    // Delete (DELETE)
    public void deleteInvestment(Long id) {
        if (!investmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Investment not found with id " + id);
        }
        investmentRepository.deleteById(id);
    }

    // Additional helper (if controllers call findByFarmer)
    public List<InvestmentDTO> getInvestmentsByFarmer(Long farmerId) {
        return investmentRepository.findByFarmerId(farmerId).stream().map(inv -> {
            InvestmentDTO d = new InvestmentDTO();
            d.setId(inv.getId());
            d.setAmount(inv.getAmount());
            d.setDescription(inv.getDescription());
            d.setDate(inv.getDate() != null ? inv.getDate().toString() : null);
            d.setFarmerId(inv.getFarmer() != null ? inv.getFarmer().getId() : null);
            return d;
        }).collect(Collectors.toList());
    }
}
