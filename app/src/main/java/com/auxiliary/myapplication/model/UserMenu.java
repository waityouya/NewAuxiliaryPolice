package com.auxiliary.myapplication.model;

public class UserMenu {
    private  int aIcon;
    private  String text;
    public UserMenu(int aIcon ,String text ){
        this.aIcon=aIcon;
        this.text=text;
    }

    public void setaIcon(int aIcon) {
        this.aIcon = aIcon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getaIcon() {
        return aIcon;
    }

    public String getText() {
        return text;
    }
}
