package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.ToolMovement;
import com.tms.toolmanagementsystem.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movements")
@CrossOrigin(origins = "http://localhost:5173")
public class MovementController {

    @Autowired
    private MovementRepository movementRepository;

    @PostMapping
    public ResponseEntity<?> recordMovement(@RequestBody ToolMovement movement) {
        boolean isRecorded = movementRepository.recordMovement(movement);

        if (isRecorded) {
            return ResponseEntity.ok("{\"status\": true, \"message\": \"Movement Recorded Successfully\"}");
        } else {
            return ResponseEntity.status(500).body("{\"status\": false, \"message\": \"Failed to record movement\"}");
        }
    }

    // Add this inside MovementController.java
    @GetMapping("/tool/{id}")
    public ResponseEntity<java.util.List<ToolMovement>> getToolHistory(@PathVariable Integer id) {
        return ResponseEntity.ok(movementRepository.getMovementsByToolId(id));
    }
}