package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.tractor.dto.TractorClientDTO;
import com.farmabook.farmAbook.tractor.dto.TractorActivityDTO;
import com.farmabook.farmAbook.tractor.entity.TractorClient;
import com.farmabook.farmAbook.tractor.entity.TractorActivity;
import com.farmabook.farmAbook.tractor.repository.TractorClientRepository;
import com.farmabook.farmAbook.tractor.repository.TractorActivityRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TractorClientService {

    private final TractorClientRepository clientRepository;
    private final FarmerRepository farmerRepository;
    private final TractorActivityRepository activityRepository;

    public TractorClientService(
            TractorClientRepository clientRepository,
            FarmerRepository farmerRepository,
            TractorActivityRepository activityRepository
    ) {
        this.clientRepository = clientRepository;
        this.farmerRepository = farmerRepository;
        this.activityRepository = activityRepository;
    }

    public TractorClientDTO addClient(TractorClientDTO dto) {
        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Farmer not found with id " + dto.getFarmerId())
                );

        TractorClient client = new TractorClient();
        client.setFarmer(farmer);
        client.setName(dto.getName());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        client.setNotes(dto.getNotes());

        TractorClient saved = clientRepository.save(client);
        return toDTO(saved);
    }

    public List<TractorClientDTO> getClientsByFarmer(Long farmerId) {

        List<TractorClient> clients = clientRepository.findByFarmerId(farmerId);

        return clients.stream().map(client -> {

            List<TractorActivity> activities =
                    activityRepository.findByClientId(client.getId());

            double totalAmount = activities.stream()
                    .mapToDouble(a -> a.getAmountEarned() != null ? a.getAmountEarned() : 0.0)
                    .sum();

            double totalPaid = activities.stream()
                    .mapToDouble(a -> a.getAmountPaid() != null ? a.getAmountPaid() : 0.0)
                    .sum();

            double pending = Math.max(totalAmount - totalPaid, 0.0);

            double totalAcres = activities.stream()
                    .mapToDouble(a -> a.getAcresWorked() != null ? a.getAcresWorked() : 0.0)
                    .sum();

            int trips = activities.size();

            // Convert base client → DTO
            TractorClientDTO dto = toDTO(client);

            dto.setTotalAmount(totalAmount);
            dto.setAmountReceived(totalPaid);
            dto.setPendingAmount(pending);
            dto.setTotalAcresWorked(totalAcres);
            dto.setTotalTrips(trips);

            return dto;

        }).collect(Collectors.toList());
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    private TractorClientDTO toDTO(TractorClient client) {
        TractorClientDTO dto = new TractorClientDTO();
        dto.setId(client.getId());
        dto.setFarmerId(client.getFarmer().getId());
        dto.setName(client.getName());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        dto.setNotes(client.getNotes());
        return dto;
    }
}
