package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.CropDTO;
import com.farmabook.farmAbook.dto.CropReturnsDTO;
import com.farmabook.farmAbook.entity.Crop;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.CropRepository;
import com.farmabook.farmAbook.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CropService {

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    // Add new crop
    public CropDTO addCrop(CropDTO dto) {
        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        Crop crop = new Crop();
        crop.setName(dto.getName());
        crop.setArea(dto.getArea());
        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            crop.setDate(LocalDate.parse(dto.getDate())); // convert String → LocalDate
        }
        crop.setFarmer(farmer);

        // Initial totals
        crop.setTotalInvestment(0.0);
        crop.setRemainingInvestment(0.0);
        crop.setTotalReturns(0.0);

        Crop saved = cropRepository.save(crop);

        dto.setId(saved.getId());
        dto.setTotalInvestment(saved.getTotalInvestment());
        dto.setRemainingInvestment(saved.getRemainingInvestment());
        dto.setTotalReturns(saved.getTotalReturns());

        return dto;
    }

    // Get all crops for a farmer
    public List<CropDTO> getCropsByFarmer(Long farmerId) {
        return cropRepository.findByFarmerId(farmerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get crops by date range
    public List<CropDTO> getCropsByFarmerAndDateRange(Long farmerId, LocalDate startDate, LocalDate endDate) {
        return cropRepository.findByFarmerIdAndDateBetween(farmerId, startDate, endDate)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CropReturnsDTO> getCropsWithReturnsForFinancialYear(Long farmerId, int year) {
        // financial year starts from May of `year` to April of `year+1`
        LocalDate startDate = LocalDate.of(year, 5, 1);   // May 1
        LocalDate endDate = LocalDate.of(year + 1, 4, 30); // April 30

        List<Crop> crops = cropRepository.findByFarmerId(farmerId);

        return crops.stream()
                .map(crop -> {
                    // sum returns in this financial year
                    double totalReturns = crop.getReturnEntries() != null ?
                            crop.getReturnEntries().stream()
                                    .filter(r -> r.getDate() != null &&
                                            !r.getDate().isBefore(startDate) &&
                                            !r.getDate().isAfter(endDate))
                                    .mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0.0)
                                    .sum() : 0.0;

                    // sum production in this financial year
                    double totalProduction = crop.getReturnEntries() != null ?
                            crop.getReturnEntries().stream()
                                    .filter(r -> r.getDate() != null &&
                                            !r.getDate().isBefore(startDate) &&
                                            !r.getDate().isAfter(endDate))
                                    .mapToDouble(r -> r.getQuantity() != null ? r.getQuantity() : 0.0)
                                    .sum() : 0.0;

                    return new CropReturnsDTO(
                            crop.getId(),
                            crop.getName(),
                            totalReturns,
                            totalProduction
                    );
                })
                .toList();
    }

    // Get crops for financial year (May → April)
    public List<CropDTO> getCropsByFinancialYear(Long farmerId, int startYear) {
        LocalDate start = LocalDate.of(startYear, 5, 1);
        LocalDate end = LocalDate.of(startYear + 1, 4, 30);
        return cropRepository.findByFarmerIdAndDateBetween(farmerId, start, end)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CropDTO> getCropsDropdownByFinancialYear(Long farmerId, int startYear) {
        // Financial year: May 1st startYear → April 30th next year
        LocalDate start = LocalDate.of(startYear, 5, 1);
        LocalDate end = LocalDate.of(startYear + 1, 4, 30);

        return cropRepository.findByFarmerIdAndDateBetween(farmerId, start, end).stream()
                .map(crop -> {
                    CropDTO dto = new CropDTO();
                    dto.setId(crop.getId());
                    dto.setName(crop.getName());
                    dto.setArea(crop.getArea());
                    // leave the rest null
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Map Crop entity → DTO
    private CropDTO mapToDTO(Crop crop) {
        CropDTO dto = new CropDTO();
        dto.setId(crop.getId());
        dto.setName(crop.getName());
        dto.setArea(crop.getArea());
        dto.setDate(crop.getDate() != null ? crop.getDate().toString() : null);
        dto.setFarmerId(crop.getFarmer() != null ? crop.getFarmer().getId() : null);
        dto.setTotalInvestment(crop.getTotalInvestment());
        dto.setRemainingInvestment(crop.getRemainingInvestment());
        dto.setTotalReturns(crop.getTotalReturns());
        return dto;
    }
}
