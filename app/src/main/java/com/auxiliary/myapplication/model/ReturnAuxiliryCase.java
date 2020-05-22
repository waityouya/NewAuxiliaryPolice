package com.auxiliary.myapplication.model;

public class ReturnAuxiliryCase extends JsonRootBean {
    private Case data;

    public Case getData() {
        return data;
    }

    public void setData(Case data) {
        this.data = data;
    }
}
