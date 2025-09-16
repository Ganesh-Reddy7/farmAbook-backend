//package com.farmabook.farmAbook.service;
//
//import com.farmabook.farmAbook.dto.UserDTO;
//import com.farmabook.farmAbook.entity.User;
//import com.farmabook.farmAbook.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Convert Entity → DTO
//    private UserDTO mapToDTO(User user) {
//        UserDTO dto = new UserDTO();
//        dto.setId(user.getId());
//        dto.setUsername(user.getUsername());
//        dto.setPhone(user.getPhone());
//        dto.setRole(user.getRole());
//        if (user.getFarmer() != null) {
//            dto.setFarmerId(user.getFarmer().getId());
//        }
//        return dto;
//    }
//
//    public List<UserDTO> getAllUsers() {
//        return userRepository.findAll()
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//
//    public UserDTO getUserById(Long id) {
//        return userRepository.findById(id)
//                .map(this::mapToDTO)
//                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
//    }
//
//    public UserDTO updateUserRole(Long id, String newRole) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
//
//        user.setRole(newRole);
//        userRepository.save(user);
//
//        return mapToDTO(user);
//    }
//
//    public void deleteUser(Long id) {
//        if (!userRepository.existsById(id)) {
//            throw new RuntimeException("User not found with id " + id);
//        }
//        userRepository.deleteById(id);
//    }
//}
