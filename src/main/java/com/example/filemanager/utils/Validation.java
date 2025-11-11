package com.example.filemanager.utils;

import com.example.filemanager.dto.AuthRequestDTO;
import software.amazon.awssdk.annotations.NotNull;

public class Validation {
    private Validation() {
    }

    public static boolean isCredentialsValid(@NotNull AuthRequestDTO authRequestDTO) {
        return !(authRequestDTO.getUsername().isBlank() || authRequestDTO.getPassword().isBlank());
    }
}
