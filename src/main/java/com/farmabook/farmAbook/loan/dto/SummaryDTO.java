package com.farmabook.farmAbook.loan.dto;

import com.farmabook.farmAbook.loan.dto.LoanPaymentDTO;
import java.util.List;
import java.util.ArrayList;
public class SummaryDTO {
    private double totalLoansGiven;
    private double totalLoansTaken;
    private double totalInterestDue;
    private double totalAmountPaid;
    private double interestToReceive; // NEW
    private double interestToPay;
    private double totalRemainingPrincipal;
    private int totalLoansGivenCount;
    private int totalLoansTakenCount;
    private List<LoanTransactionDTO> nearMaturityLoans;
    private List<LoanPaymentDTO> recentPayments;

    public double getTotalLoansGiven() {
        return totalLoansGiven;
    }

    public void setTotalLoansGiven(double totalLoansGiven) {
        this.totalLoansGiven = totalLoansGiven;
    }

    public double getTotalLoansTaken() {
        return totalLoansTaken;
    }

    public void setTotalLoansTaken(double totalLoansTaken) {
        this.totalLoansTaken = totalLoansTaken;
    }

    public double getTotalInterestDue() {
        return totalInterestDue;
    }

    public void setTotalInterestDue(double totalInterestDue) {
        this.totalInterestDue = totalInterestDue;
    }

    public double getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(double totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public double getTotalRemainingPrincipal() {
        return totalRemainingPrincipal;
    }

    public void setTotalRemainingPrincipal(double totalRemainingPrincipal) {
        this.totalRemainingPrincipal = totalRemainingPrincipal;
    }

    public int getTotalLoansGivenCount() {
        return totalLoansGivenCount;
    }

    public void setTotalLoansGivenCount(int totalLoansGivenCount) {
        this.totalLoansGivenCount = totalLoansGivenCount;
    }

    public int getTotalLoansTakenCount() {
        return totalLoansTakenCount;
    }

    public void setTotalLoansTakenCount(int totalLoansTakenCount) {
        this.totalLoansTakenCount = totalLoansTakenCount;
    }

    public List<LoanTransactionDTO> getNearMaturityLoans() {
        return nearMaturityLoans;
    }

    public void setNearMaturityLoans(List<LoanTransactionDTO> nearMaturityLoans) {
        this.nearMaturityLoans = nearMaturityLoans;
    }

    public List<LoanPaymentDTO> getRecentPayments() {
        return recentPayments;
    }

    public void setRecentPayments(List<LoanPaymentDTO> recentPayments) {
        this.recentPayments = recentPayments;
    }

    public double getInterestToReceive() { return interestToReceive; }
    public void setInterestToReceive(double interestToReceive) { this.interestToReceive = interestToReceive; }

    public double getInterestToPay() { return interestToPay; }
    public void setInterestToPay(double interestToPay) { this.interestToPay = interestToPay; }
}
