package com.farmabook.farmAbook.loan.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.loan.dto.LoanTransactionDTO;
import com.farmabook.farmAbook.loan.dto.LoanPaymentDTO;
import com.farmabook.farmAbook.loan.entity.LoanTransaction;
import com.farmabook.farmAbook.loan.exception.LoanNotFoundException;
import com.farmabook.farmAbook.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private com.farmabook.farmAbook.repository.FarmerRepository farmerRepository;

    private final String UPLOAD_DIR = "uploads/bonds/";

    public LoanTransactionDTO createLoan(LoanTransactionDTO dto , MultipartFile bondImage) throws IOException {
        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new LoanNotFoundException("Farmer not found"));

        LoanTransaction loan = new LoanTransaction();
        loan.setFarmer(farmer);
        loan.setPrincipal(dto.getPrincipal());
        loan.setRemainingPrincipal(dto.getPrincipal());
        loan.setAmountPaid(0.0);
        loan.setInterestRate(dto.getInterestRate());
        loan.setStartDate(dto.getStartDate() != null ? dto.getStartDate() : LocalDate.now());
        loan.setGiven(dto.getIsGiven());
        loan.setDescription(dto.getDescription());
        loan.setClosed(false);

        // Handle bond image
        MultipartFile bondFile = dto.getBondImageFile();
        if (bondFile != null && !bondFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + bondFile.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            bondFile.transferTo(filePath.toFile());
            loan.setBondImagePath(filePath.toString());
        }

        LoanTransaction saved = loanRepository.save(loan);
        return mapToDTO(saved);
    }

    public LoanTransactionDTO makePayment(LoanPaymentDTO paymentDTO) {
        LoanTransaction loan = loanRepository.findById(paymentDTO.getLoanId())
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getClosed()) throw new IllegalStateException("Loan already closed");

        double newPaid = loan.getAmountPaid() + paymentDTO.getAmount();
        double remaining = loan.getPrincipal() - newPaid;

        loan.setAmountPaid(newPaid);
        loan.setRemainingPrincipal(Math.max(remaining, 0.0));

        if (loan.getRemainingPrincipal() <= 0.0) {
            long days = ChronoUnit.DAYS.between(loan.getStartDate(), LocalDate.now());
            double interest = (loan.getPrincipal() * loan.getInterestRate() * days) / 100;
            loan.setFinalInterest(interest);
            loan.setClosed(true);
            loan.setEndDate(LocalDate.now());
        }

        return mapToDTO(loanRepository.save(loan));
    }

    public double calculateCurrentInterest(Long loanId) {
        LoanTransaction loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getClosed()) return loan.getFinalInterest() != null ? loan.getFinalInterest() : 0;

        long days = ChronoUnit.DAYS.between(loan.getStartDate(), LocalDate.now());
        double interest = (loan.getPrincipal() * loan.getInterestRate() * days) / 100;

        if (loan.getAmountPaid() > 0) {
            double ratio = loan.getRemainingPrincipal() / loan.getPrincipal();
            interest *= ratio;
        }

        return interest;
    }

    public List<LoanTransactionDTO> getLoansByFarmer(Long farmerId) {
        return loanRepository.findByFarmerId(farmerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private LoanTransactionDTO mapToDTO(LoanTransaction loan) {
        LoanTransactionDTO dto = new LoanTransactionDTO();
        dto.setId(loan.getId());
        dto.setFarmerId(loan.getFarmer().getId());
        dto.setPrincipal(loan.getPrincipal());
        dto.setRemainingPrincipal(loan.getRemainingPrincipal());
        dto.setAmountPaid(loan.getAmountPaid());
        dto.setInterestRate(loan.getInterestRate());
        dto.setStartDate(loan.getStartDate());
        dto.setEndDate(loan.getEndDate());
        dto.setFinalInterest(loan.getFinalInterest());
        dto.setIsClosed(loan.getClosed());
        dto.setIsGiven(loan.getGiven());
        dto.setDescription(loan.getDescription());
        dto.setBondImagePath(loan.getBondImagePath());
        return dto;
    }
}
