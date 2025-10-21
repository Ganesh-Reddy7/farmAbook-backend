package com.farmabook.farmAbook.tractor.repository;

import com.farmabook.farmAbook.tractor.entity.TractorExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TractorExpenseRepository extends JpaRepository<TractorExpense, Long> {
    List<TractorExpense> findByFarmerId(Long farmerId);
    List<TractorExpense> findByTractorId(Long tractorId);
}
