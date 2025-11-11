package com.example.filemanager.dto;

import software.amazon.awssdk.annotations.NotNull;

public class FileDTO {
    private Long id;
    private String name;
    @NotNull
    private String location;
    private FileStatus status;

    public enum FileStatus {
        ACTIVE("ACTIVE"), ARCHIVE("ARCHIVE");

        private final String value;

        FileStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public FileDTO() {
    }

    public FileDTO(Long id, String name, String location, FileStatus status) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
