package com.farmabook.farmAbook.tractor.repository;

import com.farmabook.farmAbook.tractor.entity.Tractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TractorRepository extends JpaRepository<Tractor, Long> {
    List<Tractor> findByFarmerId(Long farmerId);
}
