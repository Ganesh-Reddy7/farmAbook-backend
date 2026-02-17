package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.entity.User;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.tractor.dto.TractorClientDTO;
import com.farmabook.farmAbook.tractor.dto.TractorActivityDTO;
import com.farmabook.farmAbook.tractor.entity.TractorClient;
import com.farmabook.farmAbook.tractor.entity.TractorActivity;
import com.farmabook.farmAbook.tractor.enums.ClientType;
import com.farmabook.farmAbook.tractor.repository.TractorClientRepository;
import com.farmabook.farmAbook.tractor.repository.TractorActivityRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void ensureSystemClients(Farmer farmer, User user) {

        boolean selfExists = clientRepository.existsByFarmerIdAndClientType(farmer.getId(), ClientType.SELF);

        if (!selfExists) {
            TractorClient self = new TractorClient();
            self.setFarmer(farmer);
            self.setClientType(ClientType.SELF);
            self.setName(user.getUsername());
            self.setPhone(user.getPhone());
            self.setNotes("Self work");
            clientRepository.save(self);
        }
        boolean othersExists = clientRepository.existsByFarmerIdAndClientType(farmer.getId(), ClientType.OTHERS);
        if (!othersExists) {
            TractorClient others = new TractorClient();
            others.setFarmer(farmer);
            others.setClientType(ClientType.OTHERS);
            others.setName("Others");
            others.setNotes("Walk-in / unknown clients");
            clientRepository.save(others);
        }
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
        client.setClientType(ClientType.NORMAL);
        TractorClient saved = clientRepository.save(client);
        return toDTO(saved);
    }

    public List<TractorClientDTO> getClientsByFarmer(Long farmerId) {

        List<TractorClient> clients = clientRepository.findByFarmerId(farmerId);

        return clients.stream().map(client -> {

            List<TractorActivity> activities = activityRepository.findByClientId(client.getId());

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

            TractorClientDTO dto = toDTO(client);

            dto.setTotalAmount(totalAmount);
            dto.setAmountReceived(totalPaid);
            dto.setPendingAmount(pending);
            dto.setTotalAcresWorked(totalAcres);
            dto.setTotalTrips(trips);
            dto.setClientType(client.getClientType().name());

            return dto;

        }).collect(Collectors.toList());
    }


    public void deleteClient(Long id) {

        TractorClient client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        if (client.getClientType() != ClientType.NORMAL) {
            throw new IllegalStateException("System clients cannot be deleted");
        }
        clientRepository.delete(client);
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
