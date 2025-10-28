package com.example.filemanager.dto;

import com.example.filemanager.config.status.FileStatus;

public class FileDTO {
    private String name;
    private String location;
    private FileStatus status;

    public FileDTO() {
    }

    public FileDTO(String name, String location, FileStatus status) {
        this.name = name;
        this.location = location;
        this.status = status;
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

    @Override
    public String toString() {
        return "FileDTO{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                '}';
    }
}
