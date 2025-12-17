package com.farmabook.farmAbook.loan.service;

import com.farmabook.farmAbook.loan.dto.InterestCalculationHistoryDTO;
import com.farmabook.farmAbook.loan.dto.InterestCalculatorRequest;
import com.farmabook.farmAbook.loan.dto.InterestCalculatorResponse;
import com.farmabook.farmAbook.loan.entity.InterestCalculationHistory;
import com.farmabook.farmAbook.loan.exception.LoanNotFoundException;
import com.farmabook.farmAbook.loan.repository.InterestCalculationHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterestCalculatorService {

    @Autowired
    private InterestCalculationHistoryRepository historyRepository;

    // simple helper class
    public static class InterestDuration {
        int months;
        int days;

        InterestDuration(int months, int days) {
            this.months = months;
            this.days = days;
        }
    }

    private InterestDuration calculateDuration(LocalDate start, LocalDate end) {
        long totalDays = ChronoUnit.DAYS.between(start, end);
        if (totalDays <= 0) {
            return new InterestDuration(0, 0);
        }

        int months = (int) (totalDays / 30);
        int days = (int) (totalDays % 30);

        return new InterestDuration(months, days);
    }

    public InterestCalculatorResponse calculateSimpleInterest(
            InterestCalculatorRequest req
    ) {
        validate(req);
        double principal = req.getPrincipal();
        double monthlyRate = req.getRate() / 100.0;
        InterestDuration d = calculateDuration(
                req.getStartDate(),
                req.getEndDate()
        );
        double interestForMonths = principal * monthlyRate * d.months;
        double interestForDays = principal * (monthlyRate / 30) * d.days;
        double interest = interestForMonths + interestForDays;
        double total = principal + interest;
        saveHistory(req, "SIMPLE", interest, total);
        return buildResponse("SIMPLE", interest, total);
    }


    public InterestCalculatorResponse calculateCompoundInterest(
            InterestCalculatorRequest req
    ) {
        validate(req);
        double principal = req.getPrincipal();
        double monthlyRate = req.getRate() / 100.0;
        InterestDuration d = calculateDuration(
                req.getStartDate(),
                req.getEndDate()
        );
        int frequency = req.getCompoundingFrequency() != null ? req.getCompoundingFrequency() : 12;
        double years = d.months / 12.0;
        double annualRate = monthlyRate * 12;
        double amountAfterMonths =
                principal * Math.pow(
                        1 + (annualRate / frequency),
                        frequency * years
                );

        double dailyRate = monthlyRate / 30;
        double interestForDays = amountAfterMonths * dailyRate * d.days;

        double finalAmount = amountAfterMonths + interestForDays;
        double interest = finalAmount - principal;
        saveHistory(req, "COMPOUND", interest, finalAmount);
        return buildResponse("COMPOUND", interest, finalAmount);
    }


    private double calculateMonths(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        return Math.floor(days / 30.0);
    }

    private void validate(InterestCalculatorRequest req) {

        if (req.getPrincipal() == null || req.getPrincipal() <= 0)
            throw new IllegalArgumentException("Principal must be > 0");

        if (req.getRate() == null || req.getRate() <= 0)
            throw new IllegalArgumentException("Rate must be > 0");

        if (req.getStartDate() == null || req.getEndDate() == null)
            throw new IllegalArgumentException("Start date & end date required");

        if (req.getEndDate().isBefore(req.getStartDate()))
            throw new IllegalArgumentException("End date must be after start date");
    }

    private void saveHistory(
            InterestCalculatorRequest req,
            String type,
            double interest,
            double total
    ) {
        InterestCalculationHistory h = new InterestCalculationHistory();
        h.setCalculationType(type);
        h.setPrincipal(req.getPrincipal());
        h.setRate(req.getRate());
        h.setStartDate(req.getStartDate());
        h.setEndDate(req.getEndDate());
        h.setCompoundingFrequency(req.getCompoundingFrequency());
        h.setInterestAmount(round(interest));
        h.setTotalAmount(round(total));
        h.setCalculationDate(LocalDate.now());
        h.setFarmerId(req.getFarmerId());

        historyRepository.save(h);
    }

    private InterestCalculatorResponse buildResponse(
            String type,
            double interest,
            double total
    ) {
        InterestCalculatorResponse res = new InterestCalculatorResponse();
        res.setType(type);
        res.setInterest(round(interest));
        res.setTotalAmount(round(total));
        return res;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public List<InterestCalculationHistoryDTO> getHistoryByFarmerAndType(
            Long farmerId,
            String calculationType
    ) {
        return historyRepository.findByFarmerIdAndCalculationType(farmerId, calculationType.toUpperCase())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<InterestCalculationHistoryDTO> getHistoryByFarmerId(
            Long farmerId
    ) {
        return historyRepository.findByFarmerId(farmerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private InterestCalculationHistoryDTO mapToDTO(
            InterestCalculationHistory h
    ) {
        InterestCalculationHistoryDTO dto = new InterestCalculationHistoryDTO();
        dto.setId(h.getId());
        dto.setCalculationType(h.getCalculationType());
        dto.setPrincipal(h.getPrincipal());
        dto.setRate(h.getRate());
        dto.setStartDate(h.getStartDate());
        dto.setEndDate(h.getEndDate());
        dto.setCompoundingFrequency(h.getCompoundingFrequency());
        dto.setInterestAmount(h.getInterestAmount());
        dto.setTotalAmount(h.getTotalAmount());
        dto.setCalculationDate(h.getCalculationDate());
        dto.setFarmerId(h.getFarmerId());
        return dto;
    }
    public void deleteHistoryById(Long historyId) {
        if (!historyRepository.existsById(historyId)) {
            throw new LoanNotFoundException("Interest calculation history not found");
        }

        historyRepository.deleteById(historyId);
    }

    @Transactional
    public void deleteHistoryByFarmerAndType(Long farmerId, String calculationType) {
        boolean exists = historyRepository.existsByFarmerIdAndCalculationType(farmerId, calculationType.toUpperCase());
        if (!exists) {
            throw new LoanNotFoundException(
                    "No interest calculation history found for given farmer and type"
            );
        }
        historyRepository.deleteByFarmerIdAndCalculationType(
                farmerId,
                calculationType.toUpperCase()
        );
    }
    @Transactional
    public void deleteByFarmerId(Long farmerId) {

        boolean exists = historyRepository.existsByFarmerId(farmerId);
        if (!exists) {
            throw new LoanNotFoundException(
                    "No interest calculation history found for given farmer"
            );
        }
        historyRepository.deleteByFarmerId(farmerId);
    }

}
