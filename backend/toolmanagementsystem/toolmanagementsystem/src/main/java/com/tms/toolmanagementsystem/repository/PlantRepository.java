package com.tms.toolmanagementsystem.repository;

import com.tms.toolmanagementsystem.entity.Plant;
import com.tms.toolmanagementsystem.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlantRepository {

    public List<Plant> findAll() {
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plant";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Plant plant = new Plant();
                plant.setPlantId(rs.getInt("plant_id"));
                plant.setPlantName(rs.getString("plant_name"));
                plant.setImageName(rs.getString("image_name"));
                plants.add(plant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plants;
    }
}