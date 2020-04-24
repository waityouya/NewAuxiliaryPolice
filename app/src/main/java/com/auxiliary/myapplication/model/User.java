package com.auxiliary.myapplication.model;

public class User {
    private String userId;
    private String userName;
    private String passWord;

    public User(String userId ,String userName,String passWord){
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
    }

    public User(String userName,String passWord){
        this.userName = userName;
        this.passWord = passWord;
    }



    public String getUserId() {
        return userId;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
