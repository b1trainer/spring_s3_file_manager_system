package com.example.filemanager.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(
            RuntimeException ex) {

        ErrorResponse error = ErrorResponse.builder(ex.getCause(), ProblemDetail.forStatus(HttpStatus.BAD_REQUEST))
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(error);
    }
}
