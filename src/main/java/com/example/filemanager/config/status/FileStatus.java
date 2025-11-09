package com.example.filemanager.config.status;

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
