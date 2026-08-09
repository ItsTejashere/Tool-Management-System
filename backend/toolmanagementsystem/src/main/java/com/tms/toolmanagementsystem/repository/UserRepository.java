package com.tms.toolmanagementsystem.repository;

import com.tms.toolmanagementsystem.entity.User;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UserRepository {

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, role, plant_id, dept_id, email FROM users WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setPlantId((Integer) rs.getObject("plant_id"));
                    user.setDeptId((Integer) rs.getObject("dept_id"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserPassword(String username, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hashedPassword);
            ps.setString(2, username);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
