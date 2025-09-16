package com.farmabook.farmAbook.repository;

import com.farmabook.farmAbook.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    // You can add custom queries later if needed
}
