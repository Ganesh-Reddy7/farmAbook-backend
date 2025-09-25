package com.farmabook.farmAbook.repository;

import com.farmabook.farmAbook.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.time.LocalDateTime;
import java.time.LocalDate;



@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByFarmerId(Long farmerId);

    List<Investment> findByFarmerIdAndDateBetween(Long farmerId, LocalDate startDate, LocalDate endDate);

    List<Investment> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Investment> findByCropId(Long cropId);

    List<Investment> findByCropIdAndFarmerIdAndDateBetween(
            Long cropId, Long farmerId, LocalDate start, LocalDate end);



}
