package com.farmabook.farmAbook.controller;

import com.farmabook.farmAbook.dto.ApiResponse;
import com.farmabook.farmAbook.dto.AuthResponse;
import com.farmabook.farmAbook.dto.LoginRequest;
import com.farmabook.farmAbook.dto.UserDTO;
import com.farmabook.farmAbook.service.UserService;
import com.farmabook.farmAbook.util.ApiResponseUtil;
import com.farmabook.farmAbook.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    // Register User
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO savedUser = userService.createUser(userDTO);

        return ApiResponseUtil.success(
                HttpStatus.CREATED,
                "User registered successfully",
                savedUser
        );
    }


    // Login User
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest req) {

        UserDTO user = userService.login(req.getPhone(), req.getPassword());

        String token = jwtUtil.generateToken(String.valueOf(user.getId()), 60);

        AuthResponse authResponse = new AuthResponse(token, user);

        return ApiResponseUtil.success(
                "Login successful",
                authResponse
        );
    }


    // Get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();

        return ApiResponseUtil.success(
                "Users fetched successfully",
                users
        );
    }


    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);

        return ApiResponseUtil.success(
                "User fetched successfully",
                user
        );
    }


    // Update user role
    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserRole(
            @PathVariable Long id,
            @RequestParam String role
    ) {
        UserDTO updated = userService.updateUserRole(id, role);

        return ApiResponseUtil.success(
                "User role updated successfully",
                updated
        );
    }


    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ApiResponseUtil.success(
                "User deleted successfully",
                "OK"
        );
    }
}
