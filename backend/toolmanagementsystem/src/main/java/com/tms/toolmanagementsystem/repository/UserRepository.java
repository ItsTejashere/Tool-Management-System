package com.tms.toolmanagementsystem.repository;

import com.tms.toolmanagementsystem.entity.User;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    public List<User> findAllUsers() {
        String sql = "SELECT id, username, role, plant_id, dept_id, email FROM users";
        List<User> users = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setPlantId((Integer) rs.getObject("plant_id"));
                user.setDeptId((Integer) rs.getObject("dept_id"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean saveUser(User user) {
        String sql = "INSERT INTO users (username, password, role, plant_id, dept_id, email) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            if (user.getPlantId() != null) {
                ps.setInt(4, user.getPlantId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            if (user.getDeptId() != null) {
                ps.setInt(5, user.getDeptId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setString(6, user.getEmail());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(Integer userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> findAllRoles() {
        String sql = "SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = 'role'";
        List<String> roles = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String columnType = rs.getString("COLUMN_TYPE");
                if (columnType != null && columnType.startsWith("enum(")) {
                    String values = columnType.substring(5, columnType.length() - 1);
                    for (String role : values.split(",")) {
                        roles.add(role.replace("'", ""));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
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

    public boolean updateUser(Integer userId, User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, plant_id = ?, dept_id = ?, email = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            // Only update password if it was provided (non-empty)
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                ps.setString(2, user.getPassword());
            } else {
                // Query existing password and use it
                User existingUser = findUserById(userId);
                ps.setString(2, existingUser != null ? existingUser.getPassword() : "");
            }
            ps.setString(3, user.getRole());
            if (user.getPlantId() != null) {
                ps.setInt(4, user.getPlantId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            if (user.getDeptId() != null) {
                ps.setInt(5, user.getDeptId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setString(6, user.getEmail());
            ps.setInt(7, userId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User findUserById(Integer userId) {
        String sql = "SELECT id, username, password, role, plant_id, dept_id, email FROM users WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
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
}
