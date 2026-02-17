package com.farmabook.farmAbook.tractor.repository;

import com.farmabook.farmAbook.tractor.entity.TractorClient;
import com.farmabook.farmAbook.tractor.enums.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TractorClientRepository extends JpaRepository<TractorClient, Long> {
    List<TractorClient> findByFarmerId(Long farmerId);
    Optional<TractorClient> findByFarmerIdAndClientType(
            Long farmerId,
            ClientType clientType
    );

    boolean existsByFarmerIdAndClientTypeIn(Long farmerId, List<ClientType> self);
    boolean existsByFarmerIdAndClientType(Long id, ClientType clientType);
}
