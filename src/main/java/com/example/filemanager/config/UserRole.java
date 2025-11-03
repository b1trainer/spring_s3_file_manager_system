package com.example.filemanager.config;

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
