package com.example.filemanager.security;

import java.security.Principal;


public class CustomPrincipal implements Principal {

    private Long id;
    private String name;

    public CustomPrincipal() {
    }

    public CustomPrincipal(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
