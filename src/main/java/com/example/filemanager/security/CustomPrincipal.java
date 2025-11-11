package com.example.filemanager.security;

import java.io.Serializable;
import java.security.Principal;

public class CustomPrincipal implements Principal, Serializable {
    private Long id;
    private String name;
    private String status;

    public CustomPrincipal() {
    }

    public CustomPrincipal(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    @Override
    public String getName() {
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
