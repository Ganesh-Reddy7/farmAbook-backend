package com.farmabook.farmAbook.tractor.repository;

import com.farmabook.farmAbook.tractor.entity.TractorClient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TractorClientRepository extends JpaRepository<TractorClient, Long> {
    List<TractorClient> findByFarmerId(Long farmerId);
}
