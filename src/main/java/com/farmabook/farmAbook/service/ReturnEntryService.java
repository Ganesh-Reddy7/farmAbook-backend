package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.ReturnEntryDTO;
import com.farmabook.farmAbook.entity.Investment;
import com.farmabook.farmAbook.entity.ReturnEntry;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.entity.Crop;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.InvestmentRepository;
import com.farmabook.farmAbook.repository.ReturnEntryRepository;
import com.farmabook.farmAbook.repository.CropRepository;
import com.farmabook.farmAbook.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;


@Service
public class ReturnEntryService {
    @Autowired
    private ReturnEntryRepository returnEntryRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private CropRepository cropRepository;

    public ReturnEntryDTO createReturnEntry(ReturnEntryDTO dto) {
        Investment investment = investmentRepository.findById(dto.getInvestmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found with id " + dto.getInvestmentId()));

        ReturnEntry entry = new ReturnEntry();
        entry.setAmount(dto.getAmount());
        entry.setDate(LocalDate.parse(dto.getDate()));
        entry.setDescription(dto.getDescription());
        entry.setInvestment(investment);

        ReturnEntry saved = returnEntryRepository.save(entry);
        dto.setId(saved.getId());
        return dto;
    }

//    public List<ReturnEntryDTO> getReturnsByInvestment(Long investmentId) {
//        return returnEntryRepository.findByInvestmentId(investmentId)
//                .stream()
//                .map(r -> {
//                    ReturnEntryDTO dto = new ReturnEntryDTO();
//                    dto.setId(r.getId());
//                    dto.setAmount(r.getAmount());
//                    dto.setDate(r.getDate());
//                    dto.setDescription(r.getDescription());
//                    dto.setInvestmentId(r.getInvestment().getId());
//                    return dto;
//                }).collect(Collectors.toList());
//    }

    public ReturnEntryDTO createReturn(ReturnEntryDTO dto) {
        ReturnEntry entry = new ReturnEntry();
        entry.setAmount(dto.getAmount());
        entry.setDescription(dto.getDescription());

        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            try {
                entry.setDate(LocalDate.parse(dto.getDate())); // expects yyyy-MM-dd
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd");
            }
        }

        // Farmer is mandatory
        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id " + dto.getFarmerId()));
        entry.setFarmer(farmer);

        // Crop is optional
        if (dto.getCropId() != null) {
            Crop crop = cropRepository.findById(dto.getCropId())
                    .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id " + dto.getCropId()));

            // update both sides
            entry.setCrop(crop);
            crop.getReturnEntries().add(entry);

            // update total returns
            crop.setTotalReturns(crop.getTotalReturns() + dto.getAmount());

            cropRepository.save(crop);
        }

        ReturnEntry saved = returnEntryRepository.save(entry);
        return mapToDTO(saved);
    }

    // Get all (GET)
    public List<ReturnEntryDTO> getAllReturns() {
        return returnEntryRepository.findAll().stream().map(entry -> {
            ReturnEntryDTO d = new ReturnEntryDTO();
            d.setId(entry.getId());
            d.setAmount(entry.getAmount());
            d.setDescription(entry.getDescription());
            d.setDate(entry.getDate() != null ? entry.getDate().toString() : null);
            d.setInvestmentId(entry.getInvestment() != null ? entry.getInvestment().getId() : null);
            return d;
        }).collect(Collectors.toList());
    }

    // Get by ID (GET /{id})
    public ReturnEntryDTO getReturnById(Long id) {
        ReturnEntry entry = returnEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Return entry not found with id " + id));

        ReturnEntryDTO d = new ReturnEntryDTO();
        d.setId(entry.getId());
        d.setAmount(entry.getAmount());
        d.setDescription(entry.getDescription());
        d.setDate(entry.getDate() != null ? entry.getDate().toString() : null);
        d.setInvestmentId(entry.getInvestment() != null ? entry.getInvestment().getId() : null);
        return d;
    }

    // Delete (DELETE)
    public void deleteReturn(Long id) {
        if (!returnEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Return entry not found with id " + id);
        }
        returnEntryRepository.deleteById(id);
    }

    // Optional: returns by investment
    public List<ReturnEntryDTO> getReturnsByInvestment(Long investmentId) {
        return returnEntryRepository.findByInvestmentId(investmentId).stream().map(entry -> {
            ReturnEntryDTO d = new ReturnEntryDTO();
            d.setId(entry.getId());
            d.setAmount(entry.getAmount());
            d.setDescription(entry.getDescription());
            d.setDate(entry.getDate() != null ? entry.getDate().toString() : null);
            d.setInvestmentId(entry.getInvestment() != null ? entry.getInvestment().getId() : null);
            return d;
        }).collect(Collectors.toList());
    }

    public List<ReturnEntryDTO> getReturnsByFarmerId(Long farmerId) {
        return returnEntryRepository.findByFarmerId(farmerId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ReturnEntryDTO> getReturnsByCrop(Long cropId) {
        return returnEntryRepository.findByCropId(cropId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ReturnEntryDTO createReturnForFarmer(Long farmerId, ReturnEntryDTO dto) {
        // get latest investment for this farmer (based on investment.date)
        Investment latestInvestment = investmentRepository.findByFarmerId(farmerId)
                .stream()
                .max(Comparator.comparing(Investment::getDate))
                .orElseThrow(() -> new IllegalArgumentException("No investments found for farmer with id " + farmerId));

        ReturnEntry entry = new ReturnEntry();
        entry.setAmount(dto.getAmount());
        entry.setDescription(dto.getDescription());

        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            try {
                entry.setDate(LocalDate.parse(dto.getDate())); // expects yyyy-MM-dd
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format for return.date. Expected yyyy-MM-dd");
            }
        }

        // Link return to the chosen investment
        entry.setInvestment(latestInvestment);

        ReturnEntry saved = returnEntryRepository.save(entry);

        // map back to DTO
        ReturnEntryDTO res = new ReturnEntryDTO();
        res.setId(saved.getId());
        res.setAmount(saved.getAmount());
        res.setDescription(saved.getDescription());
        res.setDate(saved.getDate() != null ? saved.getDate().toString() : null);
        res.setInvestmentId(latestInvestment.getId());
        res.setFarmerId(latestInvestment.getFarmer().getId()); // derive farmerId from investment
        return res;
    }


    private ReturnEntryDTO mapToDTO(ReturnEntry entry) {
        ReturnEntryDTO dto = new ReturnEntryDTO();
        dto.setId(entry.getId());
        dto.setAmount(entry.getAmount());
        dto.setDescription(entry.getDescription());
        dto.setDate(entry.getDate() != null ? entry.getDate().toString() : null);
        dto.setFarmerId(entry.getFarmer().getId());
        dto.setCropId(entry.getCrop() != null ? entry.getCrop().getId() : null);
        return dto;
    }


}
