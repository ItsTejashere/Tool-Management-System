package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.User;
import com.tms.toolmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Updated to match spec: /api/auth/login
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User dbUser = userRepository.findByUsername(loginRequest.getUsername());

        if (dbUser != null && dbUser.getPassword().equals(loginRequest.getPassword())) {
            // New Success Format
            String jsonResponse = String.format(
                    "{\"status\": true, \"message\": \"Login Successful\", \"role\": \"%s\"}",
                    dbUser.getRole()
            );
            return ResponseEntity.ok(jsonResponse);
        }

        // New Failure Format
        return ResponseEntity.status(401).body("{\"status\": false, \"message\": \"Invalid Credentials\"}");
    }
}