package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.tractor.entity.Tractor;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.tractor.repository.TractorRepository;
import com.farmabook.farmAbook.tractor.dto.TractorDTO;
import com.farmabook.farmAbook.tractor.dto.TractorResponseDTO;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TractorService {

    private final TractorRepository tractorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TractorService(TractorRepository tractorRepository) {
        this.tractorRepository = tractorRepository;
    }

    // Save tractor and return as response DTO
    public TractorResponseDTO saveTractor(TractorDTO dto) {
        Farmer farmer = entityManager.find(Farmer.class, dto.getFarmerId());
        if (farmer == null) {
            throw new RuntimeException("Farmer not found with id " + dto.getFarmerId());
        }

        Tractor tractor = new Tractor();
        tractor.setSerialNumber(dto.getSerialNumber());
        tractor.setModel(dto.getModel());
        tractor.setMake(dto.getMake());
        tractor.setCapacityHp(dto.getCapacityHp());
        tractor.setStatus(dto.getStatus());
        tractor.setFarmer(farmer);

        Tractor saved = tractorRepository.save(tractor);
        return toDTO(saved);
    }

    // Get all tractors by farmer and convert to response DTOs
    public List<TractorResponseDTO> getAllTractorsByFarmer(Long farmerId) {
        List<Tractor> tractors = tractorRepository.findByFarmerId(farmerId);
        return tractors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TractorResponseDTO> getTractorById(Long id) {
        return tractorRepository.findById(id).map(this::toDTO);
    }

    public void deleteTractor(Long id) {
        tractorRepository.deleteById(id);
    }

    // Convert entity to response DTO
    private TractorResponseDTO toDTO(Tractor tractor) {
        TractorResponseDTO dto = new TractorResponseDTO();
        dto.setId(tractor.getId());
        dto.setSerialNumber(tractor.getSerialNumber());
        dto.setModel(tractor.getModel());
        dto.setMake(tractor.getMake());
        dto.setCapacityHp(tractor.getCapacityHp());
        dto.setStatus(tractor.getStatus());
        dto.setFarmerId(tractor.getFarmer().getId()); // only ID
        return dto;
    }
}
