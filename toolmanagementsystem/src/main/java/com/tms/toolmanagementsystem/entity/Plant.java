package com.tms.toolmanagementsystem.entity;

public class Plant {
    private Integer plantId;
    private String plantName;
    private String imageName;

    // Getters and Setters
    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}