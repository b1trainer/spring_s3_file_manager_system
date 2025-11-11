package com.example.filemanager.entity;

import com.example.filemanager.dto.UserDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Table("users")
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String password;
    private UserDTO.UserRole role;
    private UserDTO.UserStatus status;
    @Transient
    private Set<EventEntity> events = new HashSet<>();
    @CreatedDate
    private Instant createdAt;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO.UserRole getRole() {
        return role;
    }

    public void setRole(UserDTO.UserRole role) {
        this.role = role;
    }

    public UserDTO.UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserDTO.UserStatus status) {
        this.status = status;
    }

    public Set<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(Set<EventEntity> events) {
        this.events = events;
    }
}
