package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.ToolChangeHistory;
import com.tms.toolmanagementsystem.repository.ToolChangeHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
