package com.example.filemanager.exceptions;

public class UserBlockedException extends Exception {
    public UserBlockedException() {
        super("User is currently blocked");
    }

    public UserBlockedException(String message) {
        super(message);
    }

    public UserBlockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
