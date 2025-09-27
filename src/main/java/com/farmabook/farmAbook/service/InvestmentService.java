package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.*;
import com.farmabook.farmAbook.entity.*;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private CropRepository cropRepository;

    // Create investment without workers
    public InvestmentDTO createInvestment(InvestmentDTO dto) {
        Investment investment = new Investment();
        investment.setAmount(dto.getAmount());
        investment.setDescription(dto.getDescription());
        if (dto.getDate() != null && !dto.getDate().isBlank())
            investment.setDate(LocalDate.parse(dto.getDate()));

        if (dto.getFarmerId() != null) {
            Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
            investment.setFarmer(farmer);
        }

        if (dto.getCropId() != null) {
            Crop crop = cropRepository.findById(dto.getCropId())
                    .orElseThrow(() -> new ResourceNotFoundException("Crop not found"));
            investment.setCrop(crop);
        }

        Investment saved = investmentRepository.save(investment);
        updateCropTotals(saved.getCrop());

        return mapToDTO(saved);
    }

    // Create investment with workers
    public InvestmentDTO createInvestmentWithWorkers(InvestmentWorkerDTO dto) {
        Investment investment = new Investment();
        investment.setDescription(dto.getDescription());
        investment.setFarmer(farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found")));

        if (dto.getDate() != null && !dto.getDate().isBlank())
            investment.setDate(LocalDate.parse(dto.getDate()));

        if (dto.getCropId() != null) {
            Crop crop = cropRepository.findById(dto.getCropId())
                    .orElseThrow(() -> new ResourceNotFoundException("Crop not found"));
            investment.setCrop(crop);
        }

        Investment savedInvestment = investmentRepository.save(investment);

        double totalWages = 0;
        if (dto.getWorkers() != null) {
            for (WorkerDTO wDto : dto.getWorkers()) {
                Worker worker = new Worker();
                worker.setName(wDto.getName());
                worker.setRole(wDto.getRole());
                worker.setWage(wDto.getWage());
                worker.setPaymentDone(false);
                worker.setInvestment(savedInvestment);
                totalWages += wDto.getWage();
                workerRepository.save(worker);
            }
        }

        savedInvestment.setAmount(totalWages);
        savedInvestment.setRemainingAmount(totalWages);
        investmentRepository.save(savedInvestment);

        updateCropTotals(savedInvestment.getCrop());
        return mapToDTO(savedInvestment);
    }

    // Get all investments
    public List<InvestmentDTO> getAllInvestments() {
        return investmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get investment by ID
    public InvestmentDTO getInvestmentById(Long id) {
        Investment inv = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));
        return mapToDTO(inv);
    }

    // Delete investment
    public void deleteInvestment(Long id) {
        if (!investmentRepository.existsById(id))
            throw new ResourceNotFoundException("Investment not found");
        investmentRepository.deleteById(id);
    }

    // Get investments by financial year
    public List<InvestmentDTO> getInvestmentsByFinancialYear(int year) {
        LocalDate start = LocalDate.of(year, 5, 1);
        LocalDate end = LocalDate.of(year + 1, 4, 30);
        return investmentRepository.findByDateBetween(start, end)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Yearly summary for farmer
    public List<YearlyInvestmentSummaryDTO> getYearlySummaryForFarmer(Long farmerId, int startYear, int endYear) {
        List<YearlyInvestmentSummaryDTO> summaries = new ArrayList<>();
        for (int year = startYear; year <= endYear; year++) {
            LocalDate start = LocalDate.of(year, 5, 1);
            LocalDate end = LocalDate.of(year + 1, 4, 30);
            double total = investmentRepository.findByFarmerIdAndDateBetween(farmerId, start, end)
                    .stream().mapToDouble(Investment::getAmount).sum();
            summaries.add(new YearlyInvestmentSummaryDTO(year, total));
        }
        return summaries;
    }

    // Get investments by farmer
    public List<InvestmentDTO> getInvestmentsByFarmer(Long farmerId) {
        return investmentRepository.findByFarmerId(farmerId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Financial year with/without workers
    public List<?> getInvestmentsByFinancialYearwithWorkers(int year, boolean includeWorkers, Long farmerId) {
        LocalDate start = LocalDate.of(year, 5, 1);
        LocalDate end = LocalDate.of(year + 1, 4, 30);
        List<Investment> investments = farmerId != null ?
                investmentRepository.findByFarmerIdAndDateBetween(farmerId, start, end) :
                investmentRepository.findByDateBetween(start, end);

        if (!includeWorkers) {
            return investments.stream().map(this::mapToDTO).collect(Collectors.toList());
        } else {
            return investments.stream().map(this::mapToInvestmentWithWorkersDTO).collect(Collectors.toList());
        }
    }

    public List<InvestmentDTO> getInvestmentsByCrop(Long cropId) {
        return investmentRepository.findByCropId(cropId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<InvestmentDTO> getInvestmentsByCropAndFarmerAndYear(Long cropId, Long farmerId, int year) {
        LocalDate start = LocalDate.of(year, 5, 1);     // FY start (April 1)
        LocalDate end = LocalDate.of(year + 1, 4, 30);  // FY end (March 31 next year)

        return investmentRepository.findByCropIdAndFarmerIdAndDateBetween(cropId, farmerId, start, end)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    // ------------------ HELPERS ------------------ //

    private InvestmentDTO mapToDTO(Investment inv) {
        InvestmentDTO dto = new InvestmentDTO();
        dto.setId(inv.getId());
        dto.setAmount(inv.getAmount());
        dto.setDescription(inv.getDescription());
        dto.setDate(inv.getDate() != null ? inv.getDate().toString() : null);
        dto.setFarmerId(inv.getFarmer() != null ? inv.getFarmer().getId() : null);

        double assigned = inv.getWorkers() != null ?
                inv.getWorkers().stream().mapToDouble(Worker::getWage).sum() : 0.0;
        dto.setRemainingAmount(inv.getAmount() - assigned);

        if (inv.getCrop() != null) {
            dto.setCrop(mapCropToDTO(inv.getCrop()));
        }
        return dto;
    }

    private InvestmentWithWorkersDTO mapToInvestmentWithWorkersDTO(Investment inv) {
        InvestmentWithWorkersDTO dto = new InvestmentWithWorkersDTO();
        dto.setId(inv.getId());
        dto.setDescription(inv.getDescription());
        dto.setDate(inv.getDate() != null ? inv.getDate().toString() : null);
        dto.setAmount(inv.getAmount());
        dto.setRemainingAmount(inv.getRemainingAmount());
        dto.setFarmerId(inv.getFarmer() != null ? inv.getFarmer().getId() : null);

        if (inv.getWorkers() != null) {
            dto.setWorkers(inv.getWorkers().stream().map(this::mapWorkerToDTO).collect(Collectors.toList()));
        }

        if (inv.getCrop() != null) {
            dto.setCrop(mapCropToDTO(inv.getCrop()));
        }
        return dto;
    }

    private CropDTO mapCropToDTO(Crop crop) {
        CropDTO dto = new CropDTO();
        dto.setId(crop.getId());
        dto.setName(crop.getName());
        dto.setArea(crop.getArea());
        dto.setTotalInvestment(crop.getTotalInvestment());
        dto.setRemainingInvestment(crop.getRemainingInvestment());
        return dto;
    }

    private void updateCropTotals(Crop crop) {
        if (crop != null) {
            double totalInvested = crop.getInvestments().stream()
                    .mapToDouble(inv -> inv.getAmount() != null ? inv.getAmount() : 0.0)
                    .sum();
            double remaining = crop.getInvestments().stream()
                    .mapToDouble(inv -> inv.getRemainingAmount() != null ? inv.getRemainingAmount() : 0.0)
                    .sum();
            crop.setTotalInvestment(totalInvested);
            crop.setRemainingInvestment(remaining);
            cropRepository.save(crop);
        }
    }

    private WorkerDTO mapWorkerToDTO(Worker w) {
        WorkerDTO dto = new WorkerDTO();
        dto.setId(w.getId());
        dto.setName(w.getName());
        dto.setRole(w.getRole());
        dto.setWage(w.getWage());
        dto.setPaymentDone(w.isPaymentDone());
        dto.setInvestmentId(w.getInvestment() != null ? w.getInvestment().getId() : null);
        return dto;
    }
}
