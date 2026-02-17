package com.farmabook.farmAbook.tractor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.farmabook.farmAbook.tractor.entity.TractorSchedule;
import com.farmabook.farmAbook.tractor.enums.TractorScheduleStatus;

public interface TractorScheduleRepository extends JpaRepository<TractorSchedule, Long> {

    List<TractorSchedule> findByFarmerIdOrderByScheduleDateTimeAsc(Long farmerId);

    List<TractorSchedule> findByFarmerIdAndStatusOrderByScheduleDateTimeAsc(
            Long farmerId,
            TractorScheduleStatus status
    );
}