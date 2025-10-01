package com.farmabook.farmAbook.loan.repository;

import com.farmabook.farmAbook.loan.entity.LoanTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<LoanTransaction, Long> {
    List<LoanTransaction> findByFarmerId(Long farmerId);
    List<LoanTransaction> findByIsGiven(Boolean isGiven);
    List<LoanTransaction> findByFarmerIdAndIsGiven(Long farmerId, Boolean isGiven);
}
