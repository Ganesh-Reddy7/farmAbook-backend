package com.farmabook.farmAbook.loan.service;

import com.farmabook.farmAbook.loan.dto.*;
import com.farmabook.farmAbook.loan.entity.LoanPayment;
import com.farmabook.farmAbook.loan.entity.LoanTransaction;
import com.farmabook.farmAbook.loan.repository.LoanPaymentRepository;
import com.farmabook.farmAbook.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SummaryService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanPaymentRepository loanPaymentRepository;

    @Autowired
    private LoanService loanService;

    public SummaryDTO getFarmerSummary(Long farmerId) {

        SummaryDTO summary = new SummaryDTO();

        // Loans given and loans taken
        List<LoanTransaction> loansGiven = loanRepository.findByFarmerIdAndIsGiven(farmerId, true);
        List<LoanTransaction> loansTaken = loanRepository.findByFarmerIdAndIsGiven(farmerId, false);

        // Totals
        summary.setTotalLoansGiven(loansGiven.stream().mapToDouble(LoanTransaction::getPrincipal).sum());
        summary.setTotalLoansTaken(loansTaken.stream().mapToDouble(LoanTransaction::getPrincipal).sum());

        summary.setTotalAmountPaid(
                Stream.concat(loansGiven.stream(), loansTaken.stream())
                        .mapToDouble(LoanTransaction::getAmountPaid)
                        .sum()
        );

        summary.setTotalRemainingPrincipal(
                Stream.concat(loansGiven.stream(), loansTaken.stream())
                        .mapToDouble(LoanTransaction::getRemainingPrincipal)
                        .sum()
        );

        // ✅ Interest to be received (loans given)
        double interestToReceive = loansGiven.stream()
                .mapToDouble(loan -> loanService.calculateCurrentInterest(loan.getId()))
                .sum();

        // ✅ Interest to be paid (loans taken)
        double interestToPay = loansTaken.stream()
                .mapToDouble(loan -> loanService.calculateCurrentInterest(loan.getId()))
                .sum();

        summary.setInterestToReceive(interestToReceive);
        summary.setInterestToPay(interestToPay);

        summary.setTotalLoansGivenCount(loansGiven.size());
        summary.setTotalLoansTakenCount(loansTaken.size());

        // Near maturity loans
        List<LoanTransactionDTO> nearMaturity = loansGiven.stream()
                .filter(LoanTransaction::getNearMaturity)
                .map(loanService::mapToDTO)
                .collect(Collectors.toList());
        summary.setNearMaturityLoans(nearMaturity);

        // Recent payments (last 5)
        List<LoanPaymentDTO> recentPayments = loanPaymentRepository
                .findTop5ByLoan_Farmer_IdOrderByPaymentDateDesc(farmerId)
                .stream()
                .map(this::mapPaymentToDTO)
                .collect(Collectors.toList());
        summary.setRecentPayments(recentPayments);

        return summary;
    }

    public LoanPaymentDTO mapPaymentToDTO(LoanPayment payment) {
        LoanPaymentDTO dto = new LoanPaymentDTO();
        dto.setId(payment.getId());
        dto.setLoanId(payment.getLoan().getId());
        dto.setAmount(payment.getAmount());
        dto.setPrincipalPaid(payment.getPrincipalPaid());
        dto.setInterestPaid(payment.getInterestPaid());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}
