package com.farmabook.farmAbook.tractor.repository;

import com.farmabook.farmAbook.tractor.entity.TractorExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface TractorExpenseRepository extends JpaRepository<TractorExpense, Long> {
    List<TractorExpense> findByFarmerId(Long farmerId);
    List<TractorExpense> findByTractorId(Long tractorId);
    @Query("SELECT e FROM TractorExpense e " +
            "WHERE e.farmer.id = :farmerId " +
            "AND YEAR(e.expenseDate) = :year")
    List<TractorExpense> findByFarmerAndYear(
            @Param("farmerId") Long farmerId,
            @Param("year") int year
    );
    @Query("SELECT e FROM TractorExpense e WHERE e.farmer.id = :farmerId")
    List<TractorExpense> findByFarmer(@Param("farmerId") Long farmerId);
    @Query("""
    SELECT e FROM TractorExpense e
    WHERE e.farmer.id = :farmerId
      AND (:year IS NULL OR YEAR(e.expenseDate) = :year)
      AND (:month IS NULL OR MONTH(e.expenseDate) = :month)
    """)
    List<TractorExpense> findExpensesFlexible(
            @Param("farmerId") Long farmerId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );


}
