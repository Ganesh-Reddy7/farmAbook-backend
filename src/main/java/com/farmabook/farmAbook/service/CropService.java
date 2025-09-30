package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.CropDTO;
import com.farmabook.farmAbook.dto.CropReturnsDTO;
import com.farmabook.farmAbook.dto.CropPerformanceDTO;
import com.farmabook.farmAbook.dto.CropPerformanceReportDTO;
import com.farmabook.farmAbook.entity.Crop;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.entity.Investment;
import com.farmabook.farmAbook.entity.ReturnEntry;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.CropRepository;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.repository.InvestmentRepository;
import com.farmabook.farmAbook.repository.ReturnEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class CropService {

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private FarmerRepository farmerRepository;
    @Autowired
    private  InvestmentRepository investmentRepository;
    @Autowired
    private  ReturnEntryRepository returnEntryRepository;


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

        List<Crop> crops = cropRepository.findByFarmerIdAndDateBetween(farmerId ,startDate , endDate);

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

    public CropPerformanceReportDTO getCropPerformance(Long farmerId, Integer year) {
        LocalDate start;
        LocalDate end;

        if (year == null) {
            // all time
            start = LocalDate.of(1900, 1, 1);
            end = LocalDate.now();
        } else {
            start = LocalDate.of(year, 5, 1);          // May 1st of given year
            end = LocalDate.of(year + 1, 4, 30);       // Apr 30th next year
        }

        Map<Long, CropPerformanceDTO> map = new HashMap<>();

        // 1) Aggregate investments
        List<Investment> investments = investmentRepository.findByFarmerIdAndDateBetween(farmerId, start, end);
        for (Investment inv : investments) {
            Long cropId = inv.getCrop().getId();
            String cropName = inv.getCrop().getName();

            CropPerformanceDTO dto = map.getOrDefault(cropId,
                    new CropPerformanceDTO(cropId, cropName, 0.0, 0.0, 0.0, 0.0));

            dto.setTotalInvestment(dto.getTotalInvestment() +
                    (inv.getAmount() != null ? inv.getAmount() : 0.0));

            map.put(cropId, dto);
        }

        // 2) Aggregate returns
        List<ReturnEntry> returns = returnEntryRepository.findByFarmerIdAndDateBetween(farmerId, start, end);
        for (ReturnEntry re : returns) {
            Long cropId = re.getCrop().getId();
            String cropName = re.getCrop().getName();

            CropPerformanceDTO dto = map.getOrDefault(cropId,
                    new CropPerformanceDTO(cropId, cropName, 0.0, 0.0, 0.0, 0.0));

            dto.setTotalReturns(dto.getTotalReturns() +
                    (re.getAmount() != null ? re.getAmount() : 0.0));
            dto.setYield(dto.getYield() +
                    (re.getQuantity() != null ? re.getQuantity() : 0.0));

            map.put(cropId, dto);
        }

        // 3) Profit calculation
        map.values().forEach(dto ->
                dto.setProfit((dto.getTotalReturns() != null ? dto.getTotalReturns() : 0.0) -
                        (dto.getTotalInvestment() != null ? dto.getTotalInvestment() : 0.0)));

        List<CropPerformanceDTO> all = new ArrayList<>(map.values());

        // 4) Top and low crops
        List<CropPerformanceDTO> top = all.stream()
                .sorted(Comparator.comparing(CropPerformanceDTO::getProfit).reversed())
                .limit(5).collect(Collectors.toList());

        List<CropPerformanceDTO> low = all.stream()
                .sorted(Comparator.comparing(CropPerformanceDTO::getProfit))
                .limit(5).collect(Collectors.toList());

        // 5) Distribution (% of investment)
        double totalInvestment = all.stream().mapToDouble(CropPerformanceDTO::getTotalInvestment).sum();
        List<CropPerformanceDTO> distribution = all.stream()
                .map(c -> new CropPerformanceDTO(
                        c.getCropId(),
                        c.getCropName(),
                        totalInvestment == 0 ? 0.0 : (c.getTotalInvestment() / totalInvestment * 100),
                        c.getTotalReturns(),
                        c.getProfit(),
                        c.getYield()))
                .collect(Collectors.toList());

        return new CropPerformanceReportDTO(top, low, distribution);
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
