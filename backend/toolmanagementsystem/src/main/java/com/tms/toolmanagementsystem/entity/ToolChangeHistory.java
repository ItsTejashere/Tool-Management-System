package com.tms.toolmanagementsystem.entity;

public class ToolChangeHistory {
    private Integer historyId;
    private Integer toolId;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private String changedBy;
    private String changedAt;

    public Integer getHistoryId() { return historyId; }
    public void setHistoryId(Integer historyId) { this.historyId = historyId; }

    public Integer getToolId() { return toolId; }
    public void setToolId(Integer toolId) { this.toolId = toolId; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }

    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public String getChangedAt() { return changedAt; }
    public void setChangedAt(String changedAt) { this.changedAt = changedAt; }
}
