package com.effigo.tools.support_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.effigo.tools.support_api.dto.UserDTO;
import com.effigo.tools.support_api.exception.ResourceNotFoundException;
import com.effigo.tools.support_api.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }
        return ResponseEntity.ok(userDTO);
    }

    // Update a user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser == null) {
            throw new ResourceNotFoundException("Cannot update. User with ID " + id + " not found");
        }
        return ResponseEntity.ok(updatedUser);
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        if (userDTO == null) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
        return ResponseEntity.ok(userDTO);
    }

    // Login with username and password
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO loginRequest) {
        UserDTO userDTO = userService.loginUser(loginRequest.getUserName(), loginRequest.getPassword());
        if (userDTO == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
        return ResponseEntity.ok(userDTO);
    }

}
