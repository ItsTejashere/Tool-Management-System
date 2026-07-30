package com.tms.toolmanagementsystem.entity;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String role;

    // 🚀 NEW: Plant ID for Multi-Tenancy (Can be null for OWNER)
    private Integer plantId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }
}