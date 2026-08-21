package com.tms.toolmanagementsystem.repository;

import com.tms.toolmanagementsystem.entity.ToolMovement;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

@Repository
public class MovementRepository {

    public boolean recordMovement(ToolMovement movement) {
        DBConnection.ensureToolInstanceSerialIndex();

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            // 🚀 Start Transaction
            con.setAutoCommit(false);

            // Convert the list of serials into a comma-separated string
            String joinedSerials = null;
            if (movement.getSerials() != null && !movement.getSerials().isEmpty()) {
                joinedSerials = String.join(", ", movement.getSerials());
            }

            // Update the SQL to include involved_serials
            String sqlMove = "INSERT INTO tool_movement (tool_id, machine_id, project_id, quantity, involved_serials, movement_type, remarks) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement psMove = con.prepareStatement(sqlMove)) {
                psMove.setInt(1, movement.getToolId());
                psMove.setObject(2, movement.getMachineId(), Types.INTEGER);
                psMove.setObject(3, movement.getProjectId(), Types.INTEGER);
                psMove.setInt(4, movement.getQuantity());

                // Save the comma-separated receipt here
                psMove.setString(5, joinedSerials);

                psMove.setString(6, movement.getMovementType());
                psMove.setString(7, movement.getRemarks());
                psMove.executeUpdate();
            }

            // 🚀 STEP 2: Create or Update the Physical Serial Numbers!
            if (movement.getSerials() != null && !movement.getSerials().isEmpty()) {

                // If new stock, INSERT new serials (WITH UPSERT LOGIC FOR REUSED SERIALS)
                if ("STOCK_IN".equals(movement.getMovementType())) {
                    // 🚀 THE UPSERT FIX: Brings dead serials back to life without crashing!
                    String sqlInsert = "INSERT INTO tool_instance (tool_id, serial_number, current_status) VALUES (?, ?, 'AVAILABLE') " +
                            "ON DUPLICATE KEY UPDATE current_status = 'AVAILABLE', tool_id = VALUES(tool_id)";

                    try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                        for (String serial : movement.getSerials()) {
                            psInsert.setInt(1, movement.getToolId());
                            psInsert.setString(2, serial);
                            psInsert.addBatch();
                        }
                        psInsert.executeBatch();
                    }
                }
                // Otherwise, UPDATE existing serials
                else {
                    String newStatus = "AVAILABLE"; // Default for RETURN and SHARPEN_IN
                    if ("ISSUE".equals(movement.getMovementType())) newStatus = "IN_USE";
                    if ("SHARPEN_OUT".equals(movement.getMovementType())) newStatus = "SHARPENING";
                    if ("SCRAP".equals(movement.getMovementType())) newStatus = "DAMAGED";

                    String sqlUpdate = "UPDATE tool_instance SET current_status = ?, current_machine_id = ?, current_project_id = ? WHERE serial_number = ? AND tool_id = ?";
                    try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                        for (String serial : movement.getSerials()) {
                            psUpdate.setString(1, newStatus);

                            // Attach machine/project only if issuing
                            if ("ISSUE".equals(movement.getMovementType())) {
                                psUpdate.setObject(2, movement.getMachineId(), Types.INTEGER);
                                psUpdate.setObject(3, movement.getProjectId(), Types.INTEGER);
                            } else {
                                psUpdate.setNull(2, Types.INTEGER);
                                psUpdate.setNull(3, Types.INTEGER);
                            }

                            psUpdate.setString(4, serial);
                            psUpdate.setInt(5, movement.getToolId());
                            psUpdate.addBatch();
                        }
                        psUpdate.executeBatch();
                    }
                }
            }

            // Commit all changes
            con.commit();
            return true;

        } catch (Exception e) {
            if (con != null) { try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); } }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) { try { con.setAutoCommit(true); con.close(); } catch (Exception ex) { ex.printStackTrace(); } }
        }
    }

    // getMovementsByToolId remains exactly the same!
    public java.util.List<ToolMovement> getMovementsByToolId(Integer toolId) {
        java.util.List<ToolMovement> movements = new java.util.ArrayList<>();

        // Use LEFT JOIN to fetch the actual names from the other tables!
        String sql = "SELECT tm.*, m.machine_name, p.project_name " +
                "FROM tool_movement tm " +
                "LEFT JOIN machine m ON tm.machine_id = m.machine_id " +
                "LEFT JOIN project p ON tm.project_id = p.project_id " +
                "WHERE tm.tool_id = ? " +
                "ORDER BY tm.movement_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, toolId);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ToolMovement m = new ToolMovement();
                m.setMovementId(rs.getInt("movement_id"));
                m.setToolId(rs.getInt("tool_id"));
                m.setMachineId((Integer) rs.getObject("machine_id"));
                m.setProjectId((Integer) rs.getObject("project_id"));
                m.setQuantity(rs.getInt("quantity"));
                m.setInvolvedSerials(rs.getString("involved_serials"));
                m.setMovementType(rs.getString("movement_type"));
                m.setMovementDate(rs.getString("movement_date"));
                m.setRemarks(rs.getString("remarks"));

                // Catch the new joined names!
                m.setMachineName(rs.getString("machine_name"));
                m.setProjectName(rs.getString("project_name"));

                movements.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movements;
    }

    // 🚀 NEW: Delete a single history record
    public boolean deleteMovement(Integer movementId) {
        String sql = "DELETE FROM tool_movement WHERE movement_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, movementId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🚀 NEW: Delete ALL history for a specific tool
    public boolean clearToolHistory(Integer toolId) {
        String sql = "DELETE FROM tool_movement WHERE tool_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, toolId);
            return ps.executeUpdate() >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}