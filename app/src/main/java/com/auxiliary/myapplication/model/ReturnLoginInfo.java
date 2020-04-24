package com.auxiliary.myapplication.model;

public class ReturnLoginInfo extends JsonRootBean {
    private LoginReturnData data;

    public LoginReturnData getData() {
        return data;
    }

    public void setData(LoginReturnData data) {
        this.data = data;
    }
}
