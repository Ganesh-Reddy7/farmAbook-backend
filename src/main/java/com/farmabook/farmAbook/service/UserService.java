package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.UserDTO;
import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.entity.User;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.repository.UserRepository;
import com.farmabook.farmAbook.tractor.service.TractorClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.farmabook.farmAbook.util.JwtUtil;



import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TractorClientService tractorClientService;


    // Convert Entity → DTO
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPhone(user.getPhone());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        if (user.getFarmer() != null) {
            dto.setFarmerId(user.getFarmer().getId());
        }
        return dto;
    }

    // Create User
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new RuntimeException("Phone number already registered: " + dto.getPhone());
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        // hash password
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole().toUpperCase() : "FARMER");

        User savedUser = userRepository.save(user);

        if ("FARMER".equalsIgnoreCase(savedUser.getRole())) {
            Farmer farmer = new Farmer();
            farmer.setName(savedUser.getUsername());
            farmer.setPhoneNumber(savedUser.getPhone());
            farmer.setUser(savedUser);
            farmerRepository.save(farmer);
            tractorClientService.ensureSystemClients(farmer , savedUser);
        }

        return mapToDTO(savedUser);
    }

    public UserDTO login(String phone, String password) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(String.valueOf(user.getId()), 60); // 60 minutes
        UserDTO dto = mapToDTO(user);
        dto.setPassword(null);
        return dto;
    }
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public UserDTO updateUserRole(Long id, String newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        user.setRole(newRole);
        userRepository.save(user);

        return mapToDTO(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
}
