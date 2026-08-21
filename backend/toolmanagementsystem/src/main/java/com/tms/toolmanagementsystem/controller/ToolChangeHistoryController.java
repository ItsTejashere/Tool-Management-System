package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.ToolChangeHistory;
import com.tms.toolmanagementsystem.repository.ToolChangeHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ToolChangeHistoryController {

    @Autowired
    private ToolChangeHistoryRepository historyRepository;

    @GetMapping("/{id}/changes")
    public ResponseEntity<List<ToolChangeHistory>> getToolChangeHistory(@PathVariable Integer id) {
        return ResponseEntity.ok(historyRepository.findByToolId(id));
    }

    @DeleteMapping("/changes/{historyId}")
    public ResponseEntity<Map<String, Object>> deleteToolChangeHistory(@PathVariable Integer historyId) {
        boolean success = historyRepository.deleteById(historyId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", success);
        response.put("message", success ? "Change history deleted successfully" : "Change history record not found");
        return success ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}
