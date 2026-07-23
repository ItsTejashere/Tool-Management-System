package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.User;
import com.tms.toolmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        User dbUser = userRepository.findByUsername(loginRequest.getUsername());

        if (dbUser != null && dbUser.getPassword().equals(loginRequest.getPassword())) {
            // Simplified JSON response without the role
            return ResponseEntity.ok("{\"message\": \"Login successful\"}");
        }

        return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
    }
}