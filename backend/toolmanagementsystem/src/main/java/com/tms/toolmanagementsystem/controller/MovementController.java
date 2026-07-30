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
    // 🚀 NEW: Endpoint to delete one record
    @DeleteMapping("/{movementId}")
    public org.springframework.http.ResponseEntity<?> deleteMovement(@PathVariable Integer movementId) {
        boolean deleted = movementRepository.deleteMovement(movementId);
        if (deleted) return org.springframework.http.ResponseEntity.ok(java.util.Map.of("status", true));
        return org.springframework.http.ResponseEntity.status(500).body(java.util.Map.of("status", false));
    }

    // 🚀 NEW: Endpoint to wipe all history for a tool
    @DeleteMapping("/tool/{toolId}/clear")
    public org.springframework.http.ResponseEntity<?> clearToolHistory(@PathVariable Integer toolId) {
        boolean cleared = movementRepository.clearToolHistory(toolId);
        if (cleared) return org.springframework.http.ResponseEntity.ok(java.util.Map.of("status", true));
        return org.springframework.http.ResponseEntity.status(500).body(java.util.Map.of("status", false));
    }
}