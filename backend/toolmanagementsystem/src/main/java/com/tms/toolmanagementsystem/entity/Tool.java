package com.tms.toolmanagementsystem.entity;

public class Tool {
    private Integer toolId;
    private String toolCode;
    private String toolName;
    private Integer minimumQuantity;
    private Integer totalQuantity;
    private String storageLocation;
    private String status;

    public Integer getProjectId() {
        return projectId;
    }
    // Add this near the top
    private java.util.List<String> serials;

    // Add these at the bottom
    public java.util.List<String> getSerials() { return serials; }
    public void setSerials(java.util.List<String> serials) { this.serials = serials; }
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    private Integer projectId;
    private Integer availableQuantity;
    public Integer getToolId() { return toolId; }
    public void setToolId(Integer toolId) { this.toolId = toolId; }

    public String getToolCode() { return toolCode; }
    public void setToolCode(String toolCode) { this.toolCode = toolCode; }

    public String getToolName() { return toolName; }
    public void setToolName(String toolName) { this.toolName = toolName; }

    public Integer getMinimumQuantity() { return minimumQuantity; }
    public void setMinimumQuantity(Integer minimumQuantity) { this.minimumQuantity = minimumQuantity; }

    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }

    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
}