package com.example.filemanager.exceptions;

public class SignInException extends RuntimeException {
    public SignInException() {
        super("Sign in exception");
    }

    public SignInException(String message) {
        super(message);
    }

    public SignInException(String message, Throwable cause) {
        super(message, cause);
    }
}