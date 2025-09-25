package com.farmabook.farmAbook.repository;

import com.farmabook.farmAbook.entity.ReturnEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.time.LocalDate;


@Repository
public interface ReturnEntryRepository extends JpaRepository<ReturnEntry, Long> {
    List<ReturnEntry> findByInvestmentId(Long investmentId);
//    List<ReturnEntry> findByInvestmentId(Long investmentId);

    List<ReturnEntry> findByInvestmentFarmerIdAndDateBetween(Long farmerId, LocalDate startDate, LocalDate endDate);

    List<ReturnEntry> findByFarmerId(Long farmerId);
    List<ReturnEntry> findByCropId(Long cropId);

}
