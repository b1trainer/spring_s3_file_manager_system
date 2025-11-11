package com.example.filemanager.dto;

public class EventDTO {
    private Long id;
    private UserDTO user;
    private FileDTO file;
    private EventStatus status;
    private String timestamp;

    public enum EventStatus {
        CREATED("CREATED"), UPDATED("UPDATED"), DELETED("DELETED");

        private final String value;

        EventStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public EventDTO() {
    }

    public EventDTO(Long id, UserDTO user, FileDTO file, EventStatus status, String timestamp) {
        this.id = id;
        this.user = user;
        this.file = file;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public FileDTO getFile() {
        return file;
    }

    public void setFile(FileDTO file) {
        this.file = file;
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
