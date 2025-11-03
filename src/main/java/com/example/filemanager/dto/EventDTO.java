package com.example.filemanager.dto;

import com.example.filemanager.config.status.EventStatus;

public class EventDTO {
    private Long userId;
    private Long fileId;
    private EventStatus status;
    private String timestamp;

    public EventDTO() {
    }

    public EventDTO(Long userId, Long fileId, EventStatus status, String timestamp) {
        this.userId = userId;
        this.fileId = fileId;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
