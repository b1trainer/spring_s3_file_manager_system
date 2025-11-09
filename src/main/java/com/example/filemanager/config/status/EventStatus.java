package com.example.filemanager.config.status;

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
