//package com.farmabook.farmAbook.controller;
//
//import com.farmabook.farmAbook.dto.UserDTO;
//import com.farmabook.farmAbook.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    // Get all users
//    @GetMapping
//    public List<UserDTO> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    // Get user by id
//    @GetMapping("/{id}")
//    public UserDTO getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    // Update role of a user
//    @PutMapping("/{id}/role")
//    public UserDTO updateUserRole(@PathVariable Long id, @RequestParam String role) {
//        return userService.updateUserRole(id, role);
//    }
//
//    // Delete user
//    @DeleteMapping("/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return "User deleted successfully";
//    }
//}
