package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.InvestmentDTO;
import com.farmabook.farmAbook.dto.WorkerDTO;
import com.farmabook.farmAbook.dto.InvestmentWorkerDTO;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.entity.Investment;
import com.farmabook.farmAbook.entity.Worker;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import com.farmabook.farmAbook.repository.WorkerRepository;


@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private WorkerRepository workerRepository;


    public InvestmentDTO createInvestment(InvestmentDTO dto) {
        Investment investment = new Investment();
        investment.setAmount(dto.getAmount());
        investment.setDescription(dto.getDescription());

        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            try {
                investment.setDate(LocalDate.parse(dto.getDate()));
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd");
            }
        }

        if (dto.getFarmerId() != null) {
            Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
            investment.setFarmer(farmer);
        }

        Investment saved = investmentRepository.save(investment);
        return mapToDTO(saved);
    }

    public List<InvestmentDTO> getAllInvestments() {
        return investmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public InvestmentDTO getInvestmentById(Long id) {
        Investment inv = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));
        return mapToDTO(inv);
    }

    public void deleteInvestment(Long id) {
        if (!investmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Investment not found");
        }
        investmentRepository.deleteById(id);
    }

    public List<InvestmentDTO> getInvestmentsByFarmer(Long farmerId) {
        return investmentRepository.findByFarmerId(farmerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public InvestmentDTO createInvestmentWithWorkers(InvestmentWorkerDTO dto) {

        // Validate farmer
        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        Investment investment = new Investment();
        investment.setDescription(dto.getDescription());
        investment.setFarmer(farmer);

        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            investment.setDate(LocalDate.parse(dto.getDate()));
        }

        // Save first to get ID
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

        InvestmentDTO response = new InvestmentDTO();
        response.setId(savedInvestment.getId());
        response.setDescription(savedInvestment.getDescription());
        response.setDate(savedInvestment.getDate().toString());
        response.setAmount(savedInvestment.getAmount());
        response.setRemainingAmount(savedInvestment.getRemainingAmount());
        response.setFarmerId(savedInvestment.getFarmer().getId());

        return response;
    }

    public List<InvestmentDTO> getInvestmentsByFinancialYear(int year) {
        // May 1 of selected year
        LocalDate start = LocalDate.of(year, 5, 1);
        // April 30 of next year
        LocalDate end = LocalDate.of(year + 1, 4, 30);

        List<Investment> investments = investmentRepository.findByDateBetween(start, end);

        return investments.stream().map(inv -> {
            InvestmentDTO dto = new InvestmentDTO();
            dto.setId(inv.getId());
            dto.setDescription(inv.getDescription());
            dto.setDate(inv.getDate() != null ? inv.getDate().toString() : null);
            dto.setAmount(inv.getAmount());
            dto.setRemainingAmount(inv.getRemainingAmount());
            dto.setFarmerId(inv.getFarmer() != null ? inv.getFarmer().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }


    // ✅ helper method
    private InvestmentDTO mapToDTO(Investment inv) {
        InvestmentDTO dto = new InvestmentDTO();
        dto.setId(inv.getId());
        dto.setAmount(inv.getAmount());
        dto.setDescription(inv.getDescription());
        dto.setDate(inv.getDate() != null ? inv.getDate().toString() : null);
        dto.setFarmerId(inv.getFarmer() != null ? inv.getFarmer().getId() : null);

        double assigned = inv.getWorkers() != null ?
                inv.getWorkers().stream().mapToDouble(w -> w.getWage()).sum() : 0.0;

        dto.setRemainingAmount(inv.getAmount() - assigned);
        return dto;
    }
}
