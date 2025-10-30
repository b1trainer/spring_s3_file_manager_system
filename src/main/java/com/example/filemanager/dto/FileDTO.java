package com.example.filemanager.dto;

import com.example.filemanager.config.status.FileStatus;

public class FileDTO {
    private String name;
    private byte[] content;
    private String location;
    private String contentType;
    private FileStatus status;

    public FileDTO() {
    }

    public FileDTO(String name, String location, FileStatus status, byte[] content, String contentType) {
        this.name = name;
        this.location = location;
        this.status = status;
        this.content = content;
        this.contentType = contentType;
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
