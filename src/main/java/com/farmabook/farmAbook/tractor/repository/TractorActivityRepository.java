package com.farmabook.farmAbook.tractor.repository;

import com.farmabook.farmAbook.tractor.entity.TractorActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface TractorActivityRepository extends JpaRepository<TractorActivity, Long> {
    List<TractorActivity> findByFarmerId(Long farmerId);
    List<TractorActivity> findByTractorId(Long tractorId);
    // ✅ Add this method for client-based lookup
    List<TractorActivity> findByClientId(Long clientId);
    @Query("SELECT a FROM TractorActivity a " +
            "WHERE a.farmer.id = :farmerId " +
            "AND YEAR(a.activityDate) = :year")
    List<TractorActivity> findByFarmerAndYear(
            @Param("farmerId") Long farmerId,
            @Param("year") int year);
    @Query("""
    SELECT a FROM TractorActivity a
    WHERE a.farmer.id = :farmerId
      AND (:year IS NULL OR YEAR(a.activityDate) = :year)
      AND (:month IS NULL OR MONTH(a.activityDate) = :month)
    """)
    List<TractorActivity> findActivitiesFlexible(
            @Param("farmerId") Long farmerId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

}
