package com.farmabook.farmAbook.repository;

import com.farmabook.farmAbook.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findByInvestmentId(Long investmentId);
}
