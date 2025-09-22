package com.farmabook.farmAbook.controller;

import com.farmabook.farmAbook.dto.UserDTO;
import com.farmabook.farmAbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.farmabook.farmAbook.dto.AuthResponse;
import com.farmabook.farmAbook.util.JwtUtil;
import com.farmabook.farmAbook.dto.LoginRequest;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        UserDTO user = userService.login(req.getPhone(), req.getPassword());
        String token = jwtUtil.generateToken(String.valueOf(user.getId()), 60); // 60 min validity
        return ResponseEntity.ok(new AuthResponse(token, user));
    }
    // Get all users
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by id
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Update role of a user
    @PutMapping("/{id}/role")
    public UserDTO updateUserRole(@PathVariable Long id, @RequestParam String role) {
        return userService.updateUserRole(id, role);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}
