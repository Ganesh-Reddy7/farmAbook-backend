package com.farmabook.farmAbook.loan.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.loan.dto.LoanTransactionDTO;
import com.farmabook.farmAbook.loan.dto.LoanPaymentDTO;
import com.farmabook.farmAbook.loan.entity.LoanTransaction;
import com.farmabook.farmAbook.loan.entity.LoanPayment;
import com.farmabook.farmAbook.loan.exception.LoanNotFoundException;
import com.farmabook.farmAbook.loan.repository.LoanRepository;
import com.farmabook.farmAbook.loan.repository.LoanPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanPaymentRepository loanPaymentRepository;

    @Autowired
    private com.farmabook.farmAbook.repository.FarmerRepository farmerRepository;

    private final String UPLOAD_DIR = "uploads/bonds/";

    // ----------------- CREATE LOAN -----------------
    public LoanTransactionDTO createLoan(LoanTransactionDTO dto, MultipartFile bondImage) throws IOException {
        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new LoanNotFoundException("Farmer not found"));

        LoanTransaction loan = new LoanTransaction();
        loan.setFarmer(farmer);

        // Original and updated principal
        loan.setPrincipal(dto.getPrincipal());                  // original
        loan.setUpdatedPrincipal(dto.getPrincipal());           // used for compounding
        loan.setRemainingPrincipal(dto.getPrincipal());

        loan.setSource(dto.getSource());
        loan.setAmountPaid(0.0);
        loan.setInterestRate(dto.getInterestRate());
        loan.setStartDate(dto.getStartDate() != null ? dto.getStartDate() : LocalDate.now());
        loan.setLastCompoundedDate(loan.getStartDate());
        loan.setGiven(dto.getIsGiven());
        loan.setDescription(dto.getDescription());
        loan.setClosed(false);

        // Maturity details
        loan.setMaturityPeriodYears(dto.getMaturityPeriodYears());
        if (dto.getMaturityPeriodYears() != null && dto.getMaturityPeriodYears() > 0) {
            loan.setNextMaturityDate(loan.getStartDate().plusYears(dto.getMaturityPeriodYears()));
        }

        // Handle bond image
        MultipartFile bondFile = dto.getBondImageFile();
        if (bondFile != null && !bondFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + bondFile.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            bondFile.transferTo(filePath.toFile());
            loan.setBondImagePath(filePath.toString());
        }

        return mapToDTO(loanRepository.save(loan));
    }

    // ----------------- MAKE PAYMENT -----------------
    public LoanTransactionDTO makePayment(LoanPaymentDTO paymentDTO) {
        LoanTransaction loan = loanRepository.findById(paymentDTO.getLoanId())
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getClosed()) throw new IllegalStateException("Loan already closed");

        // Record payment
        LoanPayment payment = new LoanPayment();
        payment.setLoan(loan);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentDate(LocalDate.now());
        loanPaymentRepository.save(payment);

        // Apply compounding before updating remaining principal
        applyCompounding(loan);

        // Update totals based on updated principal
        double newPaid = loan.getAmountPaid() + paymentDTO.getAmount();
        double remaining = loan.getUpdatedPrincipal() - newPaid;

        loan.setAmountPaid(newPaid);
        loan.setRemainingPrincipal(Math.max(remaining, 0.0));

        // Close loan if fully paid
        if (loan.getRemainingPrincipal() <= 0.0) {
            double interest = calculateCurrentInterest(loan.getId());
            loan.setFinalInterest(interest);
            loan.setClosed(true);
            loan.setEndDate(LocalDate.now());
        }

        return mapToDTO(loanRepository.save(loan));
    }

    // ----------------- CALCULATE CURRENT INTEREST -----------------
    public double calculateCurrentInterest(Long loanId) {
        LoanTransaction loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        applyCompounding(loan);

        if (loan.getClosed()) return loan.getFinalInterest() != null ? loan.getFinalInterest() : 0;

        if (loan.getLastCompoundedDate() == null || loan.getUpdatedPrincipal() == null) return 0;

        // Days since last compounding
        long days = ChronoUnit.DAYS.between(loan.getLastCompoundedDate(), LocalDate.now());
        double monthlyRate = loan.getInterestRate(); // 1.5 per month
        double interest = (loan.getUpdatedPrincipal() * monthlyRate * days / 30) / 100;

        if (loan.getAmountPaid() > 0) {
            double ratio = loan.getRemainingPrincipal() / loan.getUpdatedPrincipal();
            interest *= ratio;
        }

        return interest;
    }

    // ----------------- GET LOANS BY FARMER -----------------
    public List<LoanTransactionDTO> getLoansByFarmer(Long farmerId) {
        return loanRepository.findByFarmerId(farmerId)
                .stream()
                .map(loan -> {
                    LoanTransactionDTO dto = mapToDTO(loan);
                    dto.setCurrentInterest(calculateCurrentInterest(loan.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ----------------- GET LOANS BY FARMER AND TYPE -----------------
    public List<LoanTransactionDTO> getLoansByFarmerAndType(Long farmerId, Boolean isGiven) {
        return loanRepository.findByFarmerIdAndIsGiven(farmerId, isGiven)
                .stream()
                .map(loan -> {
                    LoanTransactionDTO dto = mapToDTO(loan);
                    dto.setCurrentInterest(calculateCurrentInterest(loan.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ----------------- APPLY COMPOUNDING -----------------
    private void applyCompounding(LoanTransaction loan) {
        if (loan.getMaturityPeriodYears() == null || loan.getMaturityPeriodYears() <= 0) return;
        if (loan.getLastCompoundedDate() == null || loan.getUpdatedPrincipal() == null) return;

        LocalDate today = LocalDate.now();

        if (loan.getNextMaturityDate() != null && !today.isBefore(loan.getNextMaturityDate())) {
            long periods = ChronoUnit.YEARS.between(loan.getLastCompoundedDate(), today) / loan.getMaturityPeriodYears();
            if (periods > 0) {
                double compoundedAmount = loan.getUpdatedPrincipal() *
                        Math.pow(1 + (loan.getInterestRate() / 100), periods);

                loan.setUpdatedPrincipal(compoundedAmount);
                loan.setRemainingPrincipal(compoundedAmount - loan.getAmountPaid());

                // update compounding dates
                loan.setLastCompoundedDate(loan.getLastCompoundedDate().plusYears((int) (periods * loan.getMaturityPeriodYears())));
                loan.setNextMaturityDate(loan.getLastCompoundedDate().plusYears(loan.getMaturityPeriodYears()));
            }
        }

        loan.setNearMaturity(loan.getNextMaturityDate() != null &&
                today.plusDays(30).isAfter(loan.getNextMaturityDate()));

        loanRepository.save(loan);
    }

    // ----------------- MAP TO DTO -----------------
    private LoanTransactionDTO mapToDTO(LoanTransaction loan) {
        LoanTransactionDTO dto = new LoanTransactionDTO();
        dto.setId(loan.getId());
        dto.setFarmerId(loan.getFarmer().getId());
        dto.setPrincipal(loan.getPrincipal());               // original
        dto.setUpdatedPrincipal(loan.getUpdatedPrincipal()); // compounded
        dto.setSource(loan.getSource());
        dto.setRemainingPrincipal(loan.getRemainingPrincipal());
        dto.setAmountPaid(loan.getAmountPaid());
        dto.setInterestRate(loan.getInterestRate());
        dto.setStartDate(loan.getStartDate());
        dto.setLastCompoundedDate(loan.getLastCompoundedDate());
        dto.setEndDate(loan.getEndDate());
        dto.setFinalInterest(loan.getFinalInterest());
        dto.setIsClosed(loan.getClosed());
        dto.setIsGiven(loan.getGiven());
        dto.setDescription(loan.getDescription());
        dto.setBondImagePath(loan.getBondImagePath());
        dto.setMaturityPeriodYears(loan.getMaturityPeriodYears());
        dto.setNextMaturityDate(loan.getNextMaturityDate());
        dto.setNearMaturity(loan.getNearMaturity());
        return dto;
    }
}
