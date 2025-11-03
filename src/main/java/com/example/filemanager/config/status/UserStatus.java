package com.example.filemanager.config.status;

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
