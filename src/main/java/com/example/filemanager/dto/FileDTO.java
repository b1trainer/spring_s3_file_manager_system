package com.example.filemanager.dto;

import com.example.filemanager.config.status.FileStatus;

public class FileDTO {
    private String name;
    private String location;
    private FileStatus status;
    private Long userId;

    public FileDTO() {
    }

    public FileDTO(String name, String location, FileStatus status, Long userId) {
        this.name = name;
        this.location = location;
        this.status = status;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
