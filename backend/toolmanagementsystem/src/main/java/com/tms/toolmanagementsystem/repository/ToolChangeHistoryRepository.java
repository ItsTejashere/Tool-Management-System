package com.tms.toolmanagementsystem.repository;

import com.tms.toolmanagementsystem.entity.ToolChangeHistory;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ToolChangeHistoryRepository {

    public boolean saveChange(ToolChangeHistory change) {
        String sql = "INSERT INTO tool_change_history (tool_id, field_name, old_value, new_value, changed_by) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, change.getToolId());
            ps.setString(2, change.getFieldName());
            ps.setString(3, change.getOldValue());
            ps.setString(4, change.getNewValue());
            ps.setString(5, change.getChangedBy());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ToolChangeHistory> findByToolId(Integer toolId) {
        List<ToolChangeHistory> changes = new ArrayList<>();
        String sql = "SELECT * FROM tool_change_history WHERE tool_id = ? ORDER BY changed_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, toolId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ToolChangeHistory change = new ToolChangeHistory();
                    change.setHistoryId(rs.getInt("history_id"));
                    change.setToolId(rs.getInt("tool_id"));
                    change.setFieldName(rs.getString("field_name"));
                    change.setOldValue(rs.getString("old_value"));
                    change.setNewValue(rs.getString("new_value"));
                    change.setChangedBy(rs.getString("changed_by"));
                    change.setChangedAt(rs.getString("changed_at"));
                    changes.add(change);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return changes;
    }
}
