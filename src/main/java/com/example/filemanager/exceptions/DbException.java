package com.example.filemanager.exceptions;

public class DbException extends RuntimeException {
    public DbException() {
        super("Database operation exception");
    }

    public DbException(String message) {
        super(message);
    }

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}
