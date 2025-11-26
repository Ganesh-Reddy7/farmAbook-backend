package com.farmabook.farmAbook.tractor.repository;

import com.farmabook.farmAbook.tractor.entity.TractorActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TractorActivityRepository extends JpaRepository<TractorActivity, Long> {
    List<TractorActivity> findByFarmerId(Long farmerId);
    List<TractorActivity> findByTractorId(Long tractorId);
    // ✅ Add this method for client-based lookup
    List<TractorActivity> findByClientId(Long clientId);
}
