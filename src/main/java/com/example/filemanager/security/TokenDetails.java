package com.example.filemanager.security;

import java.time.Instant;

public class TokenDetails {
    private Long userid;
    private String token;
    private Instant issuedAt;
    private Instant expiresAt;

    public TokenDetails() {
    }

    public TokenDetails(Long userid, String token, Instant issuedAt, Instant expiresAt) {
        this.userid = userid;
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public static class Builder {
        private Long userid;
        private String token;
        private Instant issuedAt;
        private Instant expiresAt;

        public Builder userid(Long userid) {
            this.userid = userid;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder issuedAt(Instant issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public TokenDetails build() {
            return new TokenDetails(this.userid, this.token, this.issuedAt, this.expiresAt);
        }
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

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
