package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.FarmerReportDTO;
import com.farmabook.farmAbook.dto.YearlyReportDTO;
import com.farmabook.farmAbook.entity.Investment;
import com.farmabook.farmAbook.entity.ReturnEntry;
import com.farmabook.farmAbook.repository.InvestmentRepository;
import com.farmabook.farmAbook.repository.ReturnEntryRepository;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
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
                .findByFarmerIdAndDateBetween(farmerId, startDate, endDate);

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

    public List<YearlyReportDTO> getFarmerLastNYearsReport(Long farmerId, int lastNYears) {
        if (lastNYears <= 0) lastNYears = 5;

        List<YearlyReportDTO> out = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();

        for (int i = 0; i < lastNYears; i++) {
            int year = currentYear - i;
            LocalDate start = LocalDate.of(year, 5, 1);
            LocalDate end = LocalDate.of(year + 1, 4, 30);

            // investments in this FY for farmer
            List<Investment> investments = investmentRepository.findByFarmerIdAndDateBetween(farmerId, start, end);
            double invTotal = investments.stream()
                    .mapToDouble(inv -> inv.getAmount() != null ? inv.getAmount() : 0.0).sum();
            double invRemainingTotal = investments.stream()
                    .mapToDouble(inv -> inv.getRemainingAmount() != null ? inv.getRemainingAmount() : 0.0).sum();

            // returns in this FY for farmer
            List<ReturnEntry> returns = returnEntryRepository.findByFarmerIdAndDateBetween(farmerId, start, end);
            double returnTotal = returns.stream()
                    .mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0.0).sum();
            double productionTotal = returns.stream()
                    .mapToDouble(r -> r.getQuantity() != null ? r.getQuantity() : 0.0).sum();

            YearlyReportDTO dto = new YearlyReportDTO(year,
                    round(invTotal),
                    round(invRemainingTotal),
                    round(returnTotal),
                    round(productionTotal));
            out.add(dto);
        }

        // Return in chronological order: oldest -> newest
        Collections.reverse(out);
        return out;
    }

    // small helper to avoid floating point weirdness (optional)
    private Double round(double v) {
        return Math.round(v * 100.0) / 100.0; // two decimal places
    }
}
