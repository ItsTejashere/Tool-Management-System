package com.tms.toolmanagementsystem.entity;

public class Project {
    private Integer projectId;
    private String projectName;
    // Add this variable near the top
    private Integer departmentId;

    // Add these getters and setters at the bottom
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
}