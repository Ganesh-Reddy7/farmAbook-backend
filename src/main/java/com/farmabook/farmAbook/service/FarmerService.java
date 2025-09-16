package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.FarmerDTO;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmerService {
    @Autowired
    private FarmerRepository farmerRepository;

    public FarmerDTO createFarmer(FarmerDTO dto) {
        Farmer farmer = new Farmer();
        farmer.setName(dto.getName());
        farmer.setLocation(dto.getLocation());
        farmer.setPhoneNumber(dto.getPhoneNumber());
        Farmer saved = farmerRepository.save(farmer);
        dto.setId(saved.getId());
        return dto;
    }

    public List<FarmerDTO> getAllFarmers() {
        return farmerRepository.findAll()
                .stream()
                .map(f -> {
                    FarmerDTO dto = new FarmerDTO();
                    dto.setId(f.getId());
                    dto.setName(f.getName());
                    dto.setLocation(f.getLocation());
                    dto.setPhoneNumber(f.getPhoneNumber());
                    return dto;
                }).collect(Collectors.toList());
    }

    public FarmerDTO getFarmerById(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id " + id));
        FarmerDTO dto = new FarmerDTO();
        dto.setId(farmer.getId());
        dto.setName(farmer.getName());
        dto.setLocation(farmer.getLocation());
        dto.setPhoneNumber(farmer.getPhoneNumber());
        return dto;
    }

    public void deleteFarmer(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id " + id));
        farmerRepository.delete(farmer);
    }
}
