package com.example.filemanager.dto;

import com.example.filemanager.config.UserRole;
import com.example.filemanager.config.status.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import software.amazon.awssdk.annotations.NotNull;

public class UserDTO {
    @NotNull
    private String username;

    private UserStatus status;

    private UserRole role;

    @NotNull
    private String password;

    private String createdAt;

    public UserDTO() {
    }

    public UserDTO(String username, UserStatus status, UserRole role, String password, String createdAt) {
        this.username = username;
        this.status = status;
        this.role = role;
        this.password = password;
        this.createdAt = createdAt;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
