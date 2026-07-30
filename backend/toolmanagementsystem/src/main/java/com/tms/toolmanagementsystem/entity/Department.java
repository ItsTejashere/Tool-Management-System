package com.tms.toolmanagementsystem.entity;

public class Department {
    private Integer departmentId;
    private String departmentName;
    private Integer plantId;

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }
}