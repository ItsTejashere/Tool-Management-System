package com.tms.toolmanagementsystem.entity;

import java.util.List;

public class Tool {
    private Integer toolId;
    private String toolCode;
    private String toolName;
    private Integer minimumQuantity;
    private Integer totalQuantity;
    private String storageLocation;
    private String status;
    private Integer projectId;
    private List<String> serials;
    private String changedBy;

    private Integer plantId;
    private String specNumber;

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    private Integer availableQuantity;
    private Integer sharpeningQuantity;
    private Integer damagedQuantity;
    private String drawingNumber;

    public String getDrawingNumber() { return drawingNumber; }
    public void setDrawingNumber(String drawingNumber) { this.drawingNumber = drawingNumber; }
    public String getSpecNumber() { return specNumber; }
    public void setSpecNumber(String specNumber) { this.specNumber = specNumber; }

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

    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public List<String> getSerials() { return serials; }
    public void setSerials(List<String> serials) { this.serials = serials; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    // 🚀 NEW GETTERS AND SETTERS
    public Integer getSharpeningQuantity() { return sharpeningQuantity; }
    public void setSharpeningQuantity(Integer sharpeningQuantity) { this.sharpeningQuantity = sharpeningQuantity; }

    public Integer getDamagedQuantity() { return damagedQuantity; }
    public void setDamagedQuantity(Integer damagedQuantity) { this.damagedQuantity = damagedQuantity; }
}
