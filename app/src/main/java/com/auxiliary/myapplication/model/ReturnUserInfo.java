package com.auxiliary.myapplication.model;

public class ReturnUserInfo extends JsonRootBean {
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
