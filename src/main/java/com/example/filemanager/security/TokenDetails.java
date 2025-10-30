package com.example.filemanager.security;

import java.util.Date;

public class TokenDetails {
    private Long userid;
    private String token;
    private Date issuedAt;
    private Date expiresAt;

    public TokenDetails() {
    }

    public TokenDetails(Long userid, String token, Date issuedAt, Date expiresAt) {
        this.userid = userid;
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
