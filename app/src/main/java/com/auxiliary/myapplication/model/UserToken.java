package com.auxiliary.myapplication.model;

public class UserToken {
    private String userId;
    private String appToken;
    public UserToken(){

    }

    public UserToken(String userId,String token){
        this.userId = userId;
        this.appToken = token;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String token) {
        this.appToken = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
