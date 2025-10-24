package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.tractor.dto.TractorClientDTO;
import com.farmabook.farmAbook.tractor.entity.TractorClient;
import com.farmabook.farmAbook.tractor.repository.TractorClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TractorClientService {

    private final TractorClientRepository clientRepository;
    private final FarmerRepository farmerRepository;

    public TractorClientService(TractorClientRepository clientRepository, FarmerRepository farmerRepository) {
        this.clientRepository = clientRepository;
        this.farmerRepository = farmerRepository;
    }

    public TractorClientDTO addClient(TractorClientDTO dto) {
        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new EntityNotFoundException("Farmer not found with id " + dto.getFarmerId()));

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
        return clientRepository.findByFarmerId(farmerId)
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
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
