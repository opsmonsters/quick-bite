package com.opsmonsters.quick_bite.dto;


import java.util.Date;

public class OtpDto {

    private Long id;
    private String otp;
    private String userId;
    private Date createdAt;
    private Date expiresAt;
    private Boolean isUsed;

    public OtpDto() {
    }

    public OtpDto(Long id, String otp, String userId, Date createdAt, Date expiresAt, Boolean isUsed) {
        this.id = id;
        this.otp = otp;
        this.userId = userId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isUsed = isUsed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }
}
