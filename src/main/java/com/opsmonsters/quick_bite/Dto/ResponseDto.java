package com.opsmonsters.quick_bite.dto;

import java.util.Date;

public class ResponseDto {
    private int statusCode;
    private String message;
    private Object data;

    public ResponseDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ResponseDto(int statusCode, Object data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public static class Builder {
        private int statusCode;
        private String message;
        private Object data;

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseDto build() {
            return new ResponseDto(statusCode, message, data);
        }
    }

    public enum Status {
        SUCCESS(200),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        NOT_FOUND(404),
        INTERNAL_SERVER_ERROR(500);

        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public static class UserDto {
        private Long userId;


        private String firstName;


        private String lastName;


        private String email;


        private String password;


        private String phoneNumber;


        private String profileImageUrl;


        private Date createdAt;


        private Date updatedAt;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}

