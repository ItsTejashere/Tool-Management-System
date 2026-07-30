package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.Tool;
import com.tms.toolmanagementsystem.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin(origins = "http://localhost:5173")
public class ToolController {

    @Autowired
    private ToolRepository toolRepository;

    @GetMapping
    public ResponseEntity<List<Tool>> getAllTools() {
        List<Tool> tools = toolRepository.findAllTools();
        return ResponseEntity.ok(tools);
    }

    // Add this inside ToolController.java
    @PostMapping
    public ResponseEntity<?> addTool(@RequestBody Tool tool) {
        boolean isSaved = toolRepository.saveTool(tool);

        if (isSaved) {
            return ResponseEntity.ok("{\"status\": true, \"message\": \"Tool Added Successfully\"}");
        } else {
            return ResponseEntity.status(500).body("{\"status\": false, \"message\": \"Failed to add tool\"}");
        }
    }

    // Add this inside ToolController.java
    // 🚀 FIXED: Removed the extra "/tools" from the path
    @DeleteMapping("/{toolId}")
    public ResponseEntity<?> deleteTool(@PathVariable Integer toolId) {
        boolean isDeleted = toolRepository.deleteTool(toolId);

        if (isDeleted) {
            return ResponseEntity.ok().body("{\"status\": true, \"message\": \"Tool and all associated data deleted successfully\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"status\": false, \"message\": \"Failed to delete tool\"}");
        }
    }
    // Fetch a single tool by ID for the Edit Page
    // 1. Get a single tool by ID (for the Edit form)
    @GetMapping("/{id}")
    public ResponseEntity<Tool> getToolById(@PathVariable Integer id) {
        Tool tool = toolRepository.getToolById(id);
        if (tool != null) {
            return ResponseEntity.ok(tool);
        }
        return ResponseEntity.notFound().build();
    }

    // 2. Update the tool data when the user clicks 'Save Changes'
    @PutMapping("/{id}")
    public ResponseEntity<java.util.Map<String, Object>> updateTool(@PathVariable Integer id, @RequestBody Tool tool) {
        tool.setToolId(id);
        boolean success = toolRepository.updateTool(tool);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("status", success);
        response.put("message", success ? "Tool updated successfully!" : "Failed to update tool.");

        return ResponseEntity.ok(response);
    }


}