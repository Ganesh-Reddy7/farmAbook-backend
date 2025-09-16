package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.FarmerReportDTO;
import com.farmabook.farmAbook.entity.Investment;
import com.farmabook.farmAbook.entity.ReturnEntry;
import com.farmabook.farmAbook.repository.InvestmentRepository;
import com.farmabook.farmAbook.repository.ReturnEntryRepository;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final InvestmentRepository investmentRepository;
    private final ReturnEntryRepository returnEntryRepository;

    public ReportService(InvestmentRepository investmentRepository,
                         ReturnEntryRepository returnEntryRepository) {
        this.investmentRepository = investmentRepository;
        this.returnEntryRepository = returnEntryRepository;
    }

    public FarmerReportDTO generateFarmerReport(Long farmerId,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                String periodLabel) {

        // Fetch investments within range
        List<Investment> investments = investmentRepository
                .findByFarmerIdAndDateBetween(farmerId, startDate, endDate);

        // Fetch returns within range
        List<ReturnEntry> returns = returnEntryRepository
                .findByInvestmentFarmerIdAndDateBetween(farmerId, startDate, endDate);

        if (investments.isEmpty() && returns.isEmpty()) {
            throw new ResourceNotFoundException("No data found for Farmer ID " + farmerId);
        }

        double totalInvestment = investments.stream()
                .mapToDouble(Investment::getAmount)
                .sum();

        double totalReturns = returns.stream()
                .mapToDouble(ReturnEntry::getAmount)
                .sum();

        double profitOrLoss = totalReturns - totalInvestment;
        double profitPercentage = totalInvestment > 0
                ? (profitOrLoss / totalInvestment) * 100
                : 0.0;

        return new FarmerReportDTO(
                farmerId,
                totalInvestment,
                totalReturns,
                profitOrLoss,
                profitPercentage,
                periodLabel != null ? periodLabel : "Custom"
        );
    }
}
