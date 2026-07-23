package com.tms.toolmanagementsystem.repository;

import com.tms.toolmanagementsystem.entity.Department;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepository {

    // Notice we pass the plantId into the method
    public List<Department> findByPlantId(Integer plantId) {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department WHERE plant_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, plantId); 
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Department dept = new Department();
                dept.setDepartmentId(rs.getInt("department_id"));
                dept.setDepartmentName(rs.getString("department_name"));
                dept.setPlantId(rs.getInt("plant_id"));
                departments.add(dept);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return departments;
    }
}