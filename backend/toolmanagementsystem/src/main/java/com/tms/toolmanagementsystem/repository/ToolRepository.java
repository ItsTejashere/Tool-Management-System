package com.tms.toolmanagementsystem.repository;

import com.tms.toolmanagementsystem.entity.Tool;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ToolRepository {

    public List<Tool> findAllTools() {
        List<Tool> tools = new ArrayList<>();


        // 🚀 THE UPGRADE: We are now counting REAL physical instances from your new table!
        String sql = "SELECT t.*, " +
                "(SELECT COUNT(*) FROM tool_instance ti WHERE ti.tool_id = t.tool_id AND ti.current_status = 'AVAILABLE') AS available_qty, " +
                "(SELECT COUNT(*) FROM tool_instance ti WHERE ti.tool_id = t.tool_id AND ti.current_status = 'SHARPENING') AS sharpening_qty, " +
                "(SELECT COUNT(*) FROM tool_instance ti WHERE ti.tool_id = t.tool_id AND ti.current_status = 'IN_USE') AS in_use_qty, " +
                "(SELECT COUNT(*) FROM tool_instance ti WHERE ti.tool_id = t.tool_id AND ti.current_status = 'DAMAGED') AS damaged_qty " +
                "FROM tool t";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tool tool = new Tool();
                tool.setProjectId((Integer) rs.getObject("project_id"));
                tool.setToolId(rs.getInt("tool_id"));
                tool.setToolCode(rs.getString("tool_code"));
                tool.setToolName(rs.getString("tool_name"));
                tool.setMinimumQuantity(rs.getInt("minimum_quantity"));
                tool.setTotalQuantity(rs.getInt("total_quantity"));
                tool.setDrawingNumber(rs.getString("drawing_number"));
                tool.setSpecNumber(rs.getString("spec_number"));


                // (Note: If you renamed this DB column to image_name earlier, change the string below!)
                tool.setStorageLocation(rs.getString("storage_location"));

                // Map the exact physical counts
                int available = rs.getInt("available_qty");
                int sharpening = rs.getInt("sharpening_qty");
                int inUse = rs.getInt("in_use_qty");
                int damaged = rs.getInt("damaged_qty");

                // Set the available quantity for the Dashboard
                tool.setAvailableQuantity(available);

                // 🚀 ADD THESE TWO LINES SO REACT GETS THE NUMBERS!
                tool.setSharpeningQuantity(sharpening);
                tool.setDamagedQuantity(damaged);

                // Smart Status: Dynamically decide the badge color based on what physical tools exist
                if (available > 0) {
                    tool.setStatus("AVAILABLE");
                } else if (sharpening > 0) {
                    tool.setStatus("SHARPENING");
                } else if (inUse > 0) {
                    tool.setStatus("IN_USE");
                } else if (damaged > 0) {
                    tool.setStatus("DAMAGED");
                } else {
                    tool.setStatus("UNAVAILABLE");
                }

                tools.add(tool);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tools;
    }
    // 🚀 1. THE FIXED saveTool()
    public boolean saveTool(Tool tool) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // 🚀 THE FIX: drawing_number goes into the TOOL table! Added the 8th '?'
            String sqlTool = "INSERT INTO tool (tool_code, tool_name, drawing_number, spec_number, minimum_quantity, total_quantity, storage_location, status, project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement psTool = con.prepareStatement(sqlTool, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                psTool.setString(1, tool.getToolCode());
                psTool.setString(2, tool.getToolName());
                psTool.setString(3, tool.getDrawingNumber());
                psTool.setString(4, tool.getSpecNumber());
                psTool.setInt(5, tool.getMinimumQuantity());

                // The total quantity is just the size of the serials list!
                int calculatedTotal = (tool.getSerials() != null) ? tool.getSerials().size() : 0;
                psTool.setInt(6, calculatedTotal);

                psTool.setString(7, tool.getStorageLocation());
                psTool.setString(8, calculatedTotal > 0 ? "AVAILABLE" : "UNAVAILABLE");

                if (tool.getProjectId() != null) {
                    psTool.setInt(9, tool.getProjectId());
                } else {
                    psTool.setNull(9, java.sql.Types.INTEGER);
                }

                psTool.executeUpdate();

                // STEP 2: Grab the new Tool ID
                java.sql.ResultSet rs = psTool.getGeneratedKeys();
                int newToolId = -1;
                if (rs.next()) {
                    newToolId = rs.getInt(1);
                }

                // STEP 3: Insert the MANUAL serial numbers
                if (newToolId != -1 && tool.getSerials() != null && !tool.getSerials().isEmpty()) {
                    // 🚀 THE FIX: ON DUPLICATE KEY UPDATE brings it back to life instead of crashing!
                    String sqlInstance = "INSERT INTO tool_instance (tool_id, serial_number, current_status) VALUES (?, ?, 'AVAILABLE') " +
                            "ON DUPLICATE KEY UPDATE current_status = 'AVAILABLE', tool_id = VALUES(tool_id)";

                    try (PreparedStatement psInstance = con.prepareStatement(sqlInstance)) {
                        for (String manualSerial : tool.getSerials()) {
                            psInstance.setInt(1, newToolId);
                            psInstance.setString(2, manualSerial); // Serial is back to #2
                            psInstance.addBatch();
                        }
                        psInstance.executeBatch();
                    }
                }
            }
            con.commit();
            return true;

        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }
    // Add this inside ToolRepository.java
    // 3. Delete a tool AND all its associated instances and history safely
    public boolean deleteTool(Integer toolId) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            // Start a transaction: Don't permanently delete anything until the whole process succeeds
            con.setAutoCommit(false);

            // STEP A: Delete all movement history for this tool
            String sqlMovements = "DELETE FROM tool_movement WHERE tool_id = ?";
            try (PreparedStatement ps1 = con.prepareStatement(sqlMovements)) {
                ps1.setInt(1, toolId);
                ps1.executeUpdate();
            }

            // STEP B: Delete all physical serial numbers attached to this tool
            String sqlInstances = "DELETE FROM tool_instance WHERE tool_id = ?";
            try (PreparedStatement ps2 = con.prepareStatement(sqlInstances)) {
                ps2.setInt(1, toolId);
                ps2.executeUpdate();
            }

            // STEP C: Delete the actual tool catalog entry
            String sqlTool = "DELETE FROM tool WHERE tool_id = ?";
            try (PreparedStatement ps3 = con.prepareStatement(sqlTool)) {
                ps3.setInt(1, toolId);
                int rowsAffected = ps3.executeUpdate();

                // If we made it here without crashing, commit the changes to the database!
                con.commit();
                return rowsAffected > 0;
            }

        } catch (Exception e) {
            // If ANYTHING goes wrong, rollback the database to exactly how it was before we tried
            if (con != null) {
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Always restore auto-commit and close the connection
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // 1. Fetch a single tool by its ID for the Edit form (Notice table name is `tool`)
    // 1. Fetch a single tool by its ID for the Edit form
    public Tool getToolById(Integer toolId) {
        String sql = "SELECT * FROM tool WHERE tool_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, toolId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Tool tool = new Tool();

                    // 🚀 THE FIX: Tell Java to remember the Project ID!
                    tool.setProjectId((Integer) rs.getObject("project_id"));

                    tool.setToolId(rs.getInt("tool_id"));
                    tool.setToolCode(rs.getString("tool_code"));
                    tool.setToolName(rs.getString("tool_name"));
                    tool.setMinimumQuantity(rs.getInt("minimum_quantity"));
                    tool.setTotalQuantity(rs.getInt("total_quantity"));
                    tool.setStorageLocation(rs.getString("storage_location"));
                    tool.setStatus(rs.getString("status"));
                    tool.setDrawingNumber(rs.getString("drawing_number"));
                    tool.setSpecNumber(rs.getString("spec_number"));
                    return tool;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2. Update the tool details in the database (Notice table name is `tool`)
    // 🚀 UPDATED: Handles blueprint updates AND inserts new serial instances if provided
    // 🚀 UPDATED updateTool() WITH UPSERT MAGIC
    public boolean updateTool(Tool tool) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // STEP 1: Update the main tool blueprint row
            String sql = "UPDATE tool SET tool_code = ?, tool_name = ?, drawing_number = ?, spec_number = ?, minimum_quantity = ?, total_quantity = ?, storage_location = ?, status = ?, project_id = ? WHERE tool_id = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tool.getToolCode());
                ps.setString(2, tool.getToolName());
                ps.setString(3, tool.getDrawingNumber());
                ps.setString(4, tool.getSpecNumber());
                ps.setInt(5, tool.getMinimumQuantity());
                ps.setInt(6, tool.getTotalQuantity());
                ps.setString(7, tool.getStorageLocation());
                ps.setString(8, tool.getStatus());

                if (tool.getProjectId() != null) {
                    ps.setInt(9, tool.getProjectId());
                } else {
                    ps.setNull(9, java.sql.Types.INTEGER);
                }

                ps.setInt(10, tool.getToolId());
                ps.executeUpdate();
            }

            // STEP 2: Insert OR Update physical serials
            if (tool.getSerials() != null && !tool.getSerials().isEmpty()) {
                // 🚀 THE FIX: Added the ON DUPLICATE KEY UPDATE logic here!
                String sqlInstance = "INSERT INTO tool_instance (tool_id, serial_number, current_status) VALUES (?, ?, 'AVAILABLE') " +
                        "ON DUPLICATE KEY UPDATE current_status = 'AVAILABLE', tool_id = VALUES(tool_id)";

                try (PreparedStatement psInstance = con.prepareStatement(sqlInstance)) {
                    for (String serial : tool.getSerials()) {
                        if (serial != null && !serial.trim().isEmpty()) {
                            psInstance.setInt(1, tool.getToolId());
                            psInstance.setString(2, serial.trim());
                            psInstance.addBatch();
                        }
                    }
                    psInstance.executeBatch();
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }
}