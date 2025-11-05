package com.example.filemanager.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import software.amazon.awssdk.annotations.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
