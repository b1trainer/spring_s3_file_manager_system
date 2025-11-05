package com.example.filemanager.utils;

import com.example.filemanager.dto.AuthRequestDTO;

public class Validation {
    private Validation() {}

    public static boolean isCredentialsValid(AuthRequestDTO authRequestDTO) {
        return !(authRequestDTO.getUsername().isBlank() || authRequestDTO.getPassword().isBlank());
    }
}
