package com.farmabook.farmAbook.loan.repository;

import com.farmabook.farmAbook.loan.entity.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Long> {

    // Find all payments for a given loan
    List<LoanPayment> findByLoanId(Long loanId);
}
