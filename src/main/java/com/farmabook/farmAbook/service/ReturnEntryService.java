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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReturnEntryService {

    @Autowired
    private ReturnEntryRepository returnEntryRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private CropRepository cropRepository;

    // --- Create return linked directly to an investment ---
    public ReturnEntryDTO createReturnEntry(ReturnEntryDTO dto) {
        Investment investment = investmentRepository.findById(dto.getInvestmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Investment not found with id " + dto.getInvestmentId()));

        ReturnEntry entry = new ReturnEntry();
        entry.setAmount(dto.getAmount());
        entry.setQuantity(dto.getQuantity());
        entry.setDescription(dto.getDescription());
        entry.setInvestment(investment);

        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            entry.setDate(LocalDate.parse(dto.getDate()));
        } else {
            entry.setDate(LocalDate.now());
        }

        ReturnEntry saved = returnEntryRepository.save(entry);
        return mapToDTO(saved);
    }

    // --- Create return linked to farmer & optional crop ---
    public ReturnEntryDTO createReturn(ReturnEntryDTO dto) {
        ReturnEntry entry = new ReturnEntry();
        entry.setAmount(dto.getAmount());
        entry.setDescription(dto.getDescription());
        entry.setQuantity(dto.getQuantity());

        // Date handling
        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            entry.setDate(LocalDate.parse(dto.getDate())); // expects yyyy-MM-dd
        } else {
            entry.setDate(LocalDate.now());
        }

        // Farmer is mandatory
        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Farmer not found with id " + dto.getFarmerId()));
        entry.setFarmer(farmer);

        // Crop is optional
        if (dto.getCropId() != null) {
            Crop crop = cropRepository.findById(dto.getCropId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Crop not found with id " + dto.getCropId()));

            entry.setCrop(crop);

            // ✅ increment totals directly
            double newTotalReturns =
                    (crop.getTotalReturns() != null ? crop.getTotalReturns() : 0.0)
                            + (dto.getAmount() != null ? dto.getAmount() : 0.0);
            crop.setTotalReturns(newTotalReturns);

            double newTotalProduction =
                    (crop.getTotalProduction() != null ? crop.getTotalProduction() : 0.0)
                            + (dto.getQuantity() != null ? dto.getQuantity() : 0.0);
            crop.setTotalProduction(newTotalProduction);

            // 🔥 Debug print
            System.out.println("Crop " + crop.getId() + " updated: " +
                    "totalReturns=" + newTotalReturns +
                    ", totalProduction=" + newTotalProduction);
        }
        ReturnEntry saved = returnEntryRepository.save(entry);

        return mapToDTO(saved);
    }


//    private void updateCropTotals(Crop crop) {
//        Double totalReturns = returnEntryRepository
//                .findAllByCropId(crop.getId())
//                .stream()
//                .mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0.0)
//                .sum();
//
//        Double totalProduction = returnEntryRepository
//                .findAllByCropId(crop.getId())
//                .stream()
//                .mapToDouble(r -> r.getQuantity() != null ? r.getQuantity() : 0.0)
//                .sum();
//
//        crop.setTotalReturns(totalReturns);
//        crop.setTotalProduction(totalProduction);
//        cropRepository.save(crop);
//    }



    // --- Get all ---
    public List<ReturnEntryDTO> getAllReturns() {
        return returnEntryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- Get by ID ---
    public ReturnEntryDTO getReturnById(Long id) {
        ReturnEntry entry = returnEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Return entry not found with id " + id));
        return mapToDTO(entry);
    }

    // --- Delete ---
    public void deleteReturn(Long id) {
        if (!returnEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Return entry not found with id " + id);
        }
        returnEntryRepository.deleteById(id);
    }

    // --- By investment ---
    public List<ReturnEntryDTO> getReturnsByInvestment(Long investmentId) {
        return returnEntryRepository.findByInvestmentId(investmentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- By farmer ---
    public List<ReturnEntryDTO> getReturnsByFarmerId(Long farmerId) {
        return returnEntryRepository.findByFarmerId(farmerId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // --- By crop ---
    public List<ReturnEntryDTO> getReturnsByCrop(Long cropId) {
        return returnEntryRepository.findByCropId(cropId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // --- Create return for farmer (auto-links to latest investment) ---
    public ReturnEntryDTO createReturnForFarmer(Long farmerId, ReturnEntryDTO dto) {
        Investment latestInvestment = investmentRepository.findByFarmerId(farmerId)
                .stream()
                .max(Comparator.comparing(Investment::getDate))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No investments found for farmer with id " + farmerId));

        ReturnEntry entry = new ReturnEntry();
        entry.setAmount(dto.getAmount());
        entry.setQuantity(dto.getQuantity());
        entry.setDescription(dto.getDescription());

        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            entry.setDate(LocalDate.parse(dto.getDate()));
        } else {
            entry.setDate(LocalDate.now());
        }

        // Link return to the chosen investment
        entry.setInvestment(latestInvestment);
        entry.setFarmer(latestInvestment.getFarmer());

        // Crop optional in this flow too
        if (dto.getCropId() != null) {
            Crop crop = cropRepository.findById(dto.getCropId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Crop not found with id " + dto.getCropId()));

            entry.setCrop(crop);

            if (crop.getReturnEntries() == null) {
                crop.setReturnEntries(new ArrayList<>());
            }
            crop.getReturnEntries().add(entry);

            // update totals
            crop.setTotalReturns((crop.getTotalReturns() != null ? crop.getTotalReturns() : 0.0)
                    + (dto.getAmount() != null ? dto.getAmount() : 0.0));
            crop.setTotalProduction((crop.getTotalProduction() != null ? crop.getTotalProduction() : 0.0)
                    + (dto.getQuantity() != null ? dto.getQuantity() : 0.0));

            cropRepository.save(crop);
        }

        ReturnEntry saved = returnEntryRepository.save(entry);
        return mapToDTO(saved);
    }

    public List<ReturnEntryDTO> getReturnsByCropAndFarmerForFinancialYear(
            Long cropId, Long farmerId, int year) {

        // Financial year from May (year) to April (year+1)
        LocalDate startDate = LocalDate.of(year, 5, 1);
        LocalDate endDate = LocalDate.of(year + 1, 4, 30);

        List<ReturnEntry> entries = returnEntryRepository.findByCropIdAndFarmerIdAndDateBetween(cropId, farmerId, startDate, endDate);

        return entries.stream()
                .map(this::mapToDTO)
                .toList();
    }

    // --- Mapper ---
    private ReturnEntryDTO mapToDTO(ReturnEntry entry) {
        ReturnEntryDTO dto = new ReturnEntryDTO();
        dto.setId(entry.getId());
        dto.setAmount(entry.getAmount());
        dto.setQuantity(entry.getQuantity());
        dto.setDescription(entry.getDescription());
        dto.setDate(entry.getDate() != null ? entry.getDate().toString() : null);
        dto.setFarmerId(entry.getFarmer() != null ? entry.getFarmer().getId() : null);
        dto.setCropId(entry.getCrop() != null ? entry.getCrop().getId() : null);
        dto.setInvestmentId(entry.getInvestment() != null ? entry.getInvestment().getId() : null);
        return dto;
    }
}
