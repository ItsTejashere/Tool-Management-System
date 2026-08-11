package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.User;
import com.tms.toolmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAllUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAllRoles() {
        return ResponseEntity.ok(userRepository.findAllRoles());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        boolean success = userRepository.saveUser(user);
        Map<String, Object> response = new HashMap<>();
        response.put("status", success);
        response.put("message", success ? "User created successfully" : "Failed to create user");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Integer id) {
        boolean success = userRepository.deleteUser(id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", success);
        response.put("message", success ? "User deleted successfully" : "Failed to delete user");
        return ResponseEntity.ok(response);
    }
}
