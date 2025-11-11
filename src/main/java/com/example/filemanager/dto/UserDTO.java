package com.example.filemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import software.amazon.awssdk.annotations.NotNull;

import java.util.List;

public class UserDTO {
    private Long id;
    @NotNull
    private String username;
    private UserStatus status;
    private UserRole role;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String createdAt;
    private List<EventDTO> events;

    public enum UserRole {
        ADMIN("ADMIN"), MODERATOR("MODERATOR"), USER("USER");

        private final String roleName;

        UserRole(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }
    }

    public enum UserStatus {
        ACTIVE("ACTIVE"), BLOCKED("BLOCKED");

        private final String statusValue;

        UserStatus(String statusValue) {
            this.statusValue = statusValue;
        }

        public String getStatusValue() {
            return statusValue;
        }
    }

    public UserDTO() {
    }

    public UserDTO(Long id, String username, UserStatus status, UserRole role, String password, String createdAt, List<EventDTO> events) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.role = role;
        this.password = password;
        this.createdAt = createdAt;
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }
}
