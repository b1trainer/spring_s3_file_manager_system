package com.example.filemanager.dto;

import com.example.filemanager.config.status.UserStatus;

import java.util.List;

public class UserDTO {
    private String id;
    private String username;
    private UserStatus status;
    private List<EventDTO> events;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }
}
