package com.tms.toolmanagementsystem.repository;

import com.tms.toolmanagementsystem.entity.Machine;
import com.tms.toolmanagementsystem.entity.Project;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MasterDataRepository {

    public List<Machine> getAllMachines() {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT machine_id, machine_name FROM machine";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Machine m = new Machine();
                m.setMachineId(rs.getInt("machine_id"));
                m.setMachineName(rs.getString("machine_name"));
                machines.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return machines;
    }

    // Fetch projects filtered by department
    public List<Project> getProjectsByDepartment(Integer departmentId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT project_id, project_name, department_id FROM project WHERE department_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project p = new Project();
                p.setProjectId(rs.getInt("project_id"));
                p.setProjectName(rs.getString("project_name"));

                // Keep the department ID mapped just in case React needs it
                p.setDepartmentId((Integer) rs.getObject("department_id"));

                projects.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }
    // Add a new project to a specific department
    public boolean addProject(Project project) {
        String sql = "INSERT INTO project (project_name, department_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setInt(2, project.getDepartmentId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a project
    public boolean deleteProject(Integer projectId) {
        String sql = "DELETE FROM project WHERE project_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            // Note: This will fail if tools are already assigned to this project due to Foreign Keys!
            e.printStackTrace();
            return false;
        }
    }
}