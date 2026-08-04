package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.ToolInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ToolInstanceController {

    // 🚀 FIXED: We use JdbcTemplate now instead of manual DriverManager strings!
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/tool-instances/{toolId}/status/{status}")
    public ResponseEntity<List<ToolInstance>> getInstancesByStatus(
            @PathVariable Integer toolId,
            @PathVariable String status) {

        String sql = "SELECT * FROM tool_instance WHERE tool_id = ? AND current_status = ?";

        // 🚀 Executes your exact SQL query, but safely through the Railway connection pool
        List<ToolInstance> instances = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ToolInstance ti = new ToolInstance();
            ti.setInstanceId(rs.getInt("instance_id"));
            ti.setToolId(rs.getInt("tool_id"));
            ti.setSerialNumber(rs.getString("serial_number"));
            ti.setCurrentStatus(rs.getString("current_status"));
            return ti;
        }, toolId, status);

        return ResponseEntity.ok(instances);
    }
}
