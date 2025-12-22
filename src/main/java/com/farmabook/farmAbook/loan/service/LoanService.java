package com.farmabook.farmAbook.loan.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.loan.dto.LoanPaymentDTO;
import com.farmabook.farmAbook.loan.dto.LoanTransactionDTO;
import com.farmabook.farmAbook.loan.entity.LoanPayment;
import com.farmabook.farmAbook.loan.entity.LoanTransaction;
import com.farmabook.farmAbook.loan.exception.LoanNotFoundException;
import com.farmabook.farmAbook.loan.repository.LoanPaymentRepository;
import com.farmabook.farmAbook.loan.repository.LoanRepository;
import com.farmabook.farmAbook.repository.FarmerRepository;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanPaymentRepository loanPaymentRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    private static final String UPLOAD_DIR = "uploads/bonds/";

    public LoanTransactionDTO createLoan(
            LoanTransactionDTO dto,
            MultipartFile bondImage
    ) throws IOException {

        Farmer farmer = farmerRepository.findById(dto.getFarmerId()).orElseThrow(() -> new LoanNotFoundException("Farmer not found"));

        LoanTransaction loan = new LoanTransaction();
        loan.setFarmer(farmer);
        loan.setPrincipal(dto.getPrincipal());
        loan.setUpdatedPrincipal(dto.getPrincipal());
        loan.setRemainingPrincipal(dto.getPrincipal());
        loan.setAmountPaid(0.0);

        loan.setInterestRate(dto.getInterestRate()); // ANNUAL %
        loan.setStartDate(dto.getStartDate() != null ? dto.getStartDate() : LocalDate.now());
        loan.setLastCompoundedDate(loan.getStartDate());

        loan.setSource(dto.getSource());
        loan.setGiven(dto.getIsGiven());
        loan.setDescription(dto.getDescription());
        loan.setClosed(false);

        if (dto.getMaturityPeriodYears() != null && dto.getMaturityPeriodYears() > 0) {
            loan.setMaturityPeriodYears(dto.getMaturityPeriodYears());
            loan.setNextMaturityDate(
                    loan.getStartDate().plusYears(dto.getMaturityPeriodYears())
            );
        }

        if (bondImage != null && !bondImage.isEmpty()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String safeName = UUID.randomUUID() + "_" + bondImage.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR, safeName);
            bondImage.transferTo(path.toFile());
            loan.setBondImagePath(path.toString());
        }

        return mapToDTO(loanRepository.save(loan));
    }
    private InterestCalculatorService.InterestDuration calculateDuration(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            return new InterestCalculatorService.InterestDuration(0, 0);
        }

        int months = 0;
        LocalDate temp = start;

        while (!temp.plusMonths(1).isAfter(end)) {
            temp = temp.plusMonths(1);
            months++;
        }
        int days = (int) ChronoUnit.DAYS.between(temp, end);

        return new InterestCalculatorService.InterestDuration(months, days);
    }

    private double calculateInterest(
            double principal,
            double monthlyRatePercent,
            LocalDate from,
            LocalDate to
    ) {
        if (to.isBefore(from)) return 0.0;

        // Calendar-based duration
        InterestCalculatorService.InterestDuration d = calculateDuration(from, to);

        double monthlyRate = monthlyRatePercent / 100.0;
        double monthlyInterest = principal * monthlyRate;
        double dailyInterest = monthlyInterest / 30;

        return (monthlyInterest * d.months) +
                (dailyInterest * d.days);
    }


    private void applyCompounding(LoanTransaction loan) {

        if (loan.getMaturityPeriodYears() == null ||
                loan.getMaturityPeriodYears() <= 0 ||
                loan.getNextMaturityDate() == null) {
            return;
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(loan.getNextMaturityDate())) {
            return;
        }

        // Calculate interest ONLY till maturity date
        double interestTillMaturity = calculateInterest(
                loan.getUpdatedPrincipal(),
                loan.getInterestRate(), // monthly %
                loan.getLastCompoundedDate(),
                loan.getNextMaturityDate()
        );

        double newPrincipal = loan.getUpdatedPrincipal() + interestTillMaturity;

        loan.setUpdatedPrincipal(newPrincipal);
        loan.setRemainingPrincipal(
                Math.max(newPrincipal - loan.getAmountPaid(), 0.0)
        );

        // Move compounding window forward
        loan.setLastCompoundedDate(loan.getNextMaturityDate());
        loan.setNextMaturityDate(
                loan.getNextMaturityDate().plusYears(loan.getMaturityPeriodYears())
        );

        loan.setNearMaturity(
                ChronoUnit.DAYS.between(today, loan.getNextMaturityDate()) <= 30
        );

        loanRepository.save(loan);
    }


    public LoanTransactionDTO makePayment(LoanPaymentDTO paymentDTO) {

        LoanTransaction loan = loanRepository.findById(paymentDTO.getId())
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getClosed())
            throw new IllegalStateException("Loan already closed");

        LocalDate today = LocalDate.now();

        // Apply compounding if maturity crossed
        applyCompounding(loan);

        // Calculate interest till today
        double interestDue = calculateInterest(
                loan.getUpdatedPrincipal(),
                loan.getInterestRate(), // monthly %
                loan.getLastCompoundedDate(),
                today
        );

        double paymentAmount = paymentDTO.getAmount();
        double interestPaid = Math.min(paymentAmount, interestDue);
        double principalPaid = Math.max(paymentAmount - interestPaid, 0.0);

        loan.setAmountPaid(loan.getAmountPaid() + paymentAmount);
        loan.setRemainingPrincipal(
                Math.max(loan.getRemainingPrincipal() - principalPaid, 0.0)
        );

        // Move interest window ONLY if interest was paid
        if (interestPaid > 0) {
            loan.setLastCompoundedDate(today);
        }

        LoanPayment payment = new LoanPayment();
        payment.setLoan(loan);
        payment.setAmount(paymentAmount);
        payment.setInterestPaid(interestPaid);
        payment.setPrincipalPaid(principalPaid);
        payment.setPaymentDate(today);
        loanPaymentRepository.save(payment);

        if (loan.getRemainingPrincipal() == 0.0) {
            loan.setClosed(true);
            loan.setEndDate(today);
        }

        return mapToDTO(loanRepository.save(loan));
    }


    public double calculateCurrentInterest(Long loanId) {

        LoanTransaction loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        return calculateInterest(
                loan.getUpdatedPrincipal(),
                loan.getInterestRate(), // monthly %
                loan.getLastCompoundedDate(),
                LocalDate.now()
        );
    }


    public List<LoanTransactionDTO> getLoansByFarmer(Long farmerId) {
        return loanRepository.findByFarmerId(farmerId)
                .stream()
                .map(loan -> {
                    LoanTransactionDTO dto = mapToDTO(loan);
                    dto.setCurrentInterest(
                            calculateInterest(
                                    loan.getUpdatedPrincipal(),
                                    loan.getInterestRate(),
                                    loan.getLastCompoundedDate(),
                                    LocalDate.now()
                            )
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<LoanTransactionDTO> getLoansByFarmerAndType(
            Long farmerId,
            Boolean isGiven
    ) {
        return loanRepository.findByFarmerIdAndIsGiven(farmerId, isGiven)
                .stream()
                .map(loan -> {
                    LoanTransactionDTO dto = mapToDTO(loan);
                    dto.setCurrentInterest(
                            calculateInterest(
                                    loan.getUpdatedPrincipal(),
                                    loan.getInterestRate(),
                                    loan.getLastCompoundedDate(),
                                    LocalDate.now()
                            )
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public LoanTransactionDTO closeLoan(Long loanId) {

        LoanTransaction loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getClosed())
            throw new IllegalStateException("Loan already closed");

        LocalDate today = LocalDate.now();

        applyCompounding(loan);

        double finalInterest = calculateInterest(
                loan.getUpdatedPrincipal(),
                loan.getInterestRate(),
                loan.getLastCompoundedDate(),
                today
        );

        loan.setFinalInterest(finalInterest);
        loan.setRemainingPrincipal(0.0);
        loan.setClosed(true);
        loan.setEndDate(today);

        return mapToDTO(loanRepository.save(loan));
    }

    public LoanTransactionDTO mapToDTO(LoanTransaction loan) {
        LoanTransactionDTO dto = new LoanTransactionDTO();
        dto.setId(loan.getId());
        dto.setFarmerId(loan.getFarmer().getId());
        dto.setPrincipal(loan.getPrincipal());
        dto.setUpdatedPrincipal(loan.getUpdatedPrincipal());
        dto.setRemainingPrincipal(loan.getRemainingPrincipal());
        dto.setAmountPaid(loan.getAmountPaid());
        dto.setInterestRate(loan.getInterestRate());
        dto.setStartDate(loan.getStartDate());
        dto.setLastCompoundedDate(loan.getLastCompoundedDate());
        dto.setEndDate(loan.getEndDate());
        dto.setFinalInterest(loan.getFinalInterest());
        dto.setIsClosed(loan.getClosed());
        dto.setIsGiven(loan.getGiven());
        dto.setSource(loan.getSource());
        dto.setDescription(loan.getDescription());
        dto.setBondImagePath(loan.getBondImagePath());
        dto.setMaturityPeriodYears(loan.getMaturityPeriodYears());
        dto.setNextMaturityDate(loan.getNextMaturityDate());
        dto.setNearMaturity(
                loan.getNextMaturityDate() != null &&
                        ChronoUnit.DAYS.between(LocalDate.now(), loan.getNextMaturityDate()) <= 30
        );
        return dto;
    }


    public List<LoanPaymentDTO> getPaymentsByLoanId(Long loanId) {

        loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        return loanPaymentRepository.findByLoanId(loanId)
                .stream()
                .map(p -> {
                    LoanPaymentDTO dto = new LoanPaymentDTO();
                    dto.setId(p.getId());
                    dto.setAmount(p.getAmount());
                    dto.setInterestPaid(p.getInterestPaid());
                    dto.setPrincipalPaid(p.getPrincipalPaid());
                    dto.setPaymentDate(p.getPaymentDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
