package com.auxiliary.myapplication.model;

public class Page extends UserToken{
    int page;
    public Page(int page,String userId,String token){
        super(userId, token);
        this.page = page;

    }

    public Page(String userId,String token){
        super(userId, token);

    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
