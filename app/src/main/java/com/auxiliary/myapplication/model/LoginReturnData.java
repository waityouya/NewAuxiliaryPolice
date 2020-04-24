package com.auxiliary.myapplication.model;

public class LoginReturnData {
    private String userId;
    private String appToken;

    public String getUserId() {
        return userId;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAppToken(String token) {
        this.appToken = token;
    }
}
