package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.ToolInstance;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ToolInstanceController {

    // Fetch serial numbers based on Tool ID and their current status
    @GetMapping("/tool-instances/{toolId}/status/{status}")
    public ResponseEntity<List<ToolInstance>> getInstancesByStatus(
            @PathVariable Integer toolId,
            @PathVariable String status) {

        List<ToolInstance> instances = new ArrayList<>();
        String sql = "SELECT * FROM tool_instance WHERE tool_id = ? AND current_status = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, toolId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ToolInstance ti = new ToolInstance();
                ti.setInstanceId(rs.getInt("instance_id"));
                ti.setToolId(rs.getInt("tool_id"));
                ti.setSerialNumber(rs.getString("serial_number"));
                ti.setCurrentStatus(rs.getString("current_status"));
                instances.add(ti);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return ResponseEntity.ok(instances);
    }


}