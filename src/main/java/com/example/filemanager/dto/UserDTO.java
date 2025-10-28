package com.example.filemanager.dto;

import com.example.filemanager.config.status.UserStatus;

import java.util.List;

public class UserDTO {
    private String username;
    private UserStatus status;
    private List<EventDTO> events;

    public UserDTO() {
    }

    public UserDTO(String username, UserStatus status, List<EventDTO> events) {
        this.username = username;
        this.status = status;
        this.events = events;
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

    @Override
    public String toString() {
        return "UserDTO{" +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", events=" + events +
                '}';
    }
}
