package com.farmabook.farmAbook.loan.repository;

import com.farmabook.farmAbook.loan.entity.InterestCalculationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestCalculationHistoryRepository
        extends JpaRepository<InterestCalculationHistory, Long> {

    List<InterestCalculationHistory> findByFarmerId(Long farmerId);

    List<InterestCalculationHistory> findByFarmerIdAndCalculationType(
            Long farmerId,
            String calculationType
    );

    boolean existsByFarmerId(Long farmerId);

    boolean existsByFarmerIdAndCalculationType(
            Long farmerId,
            String calculationType
    );

    long deleteByFarmerId(Long farmerId);

    long deleteByFarmerIdAndCalculationType(
            Long farmerId,
            String calculationType
    );
}


