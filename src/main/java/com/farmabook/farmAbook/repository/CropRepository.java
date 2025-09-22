package com.farmabook.farmAbook.repository;

import com.farmabook.farmAbook.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;


import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {

    List<Crop> findByFarmerId(Long farmerId);

    // Filter crops by date range instead of financial year
    List<Crop> findByFarmerIdAndDateBetween(Long farmerId, LocalDate startDate, LocalDate endDate);
}
