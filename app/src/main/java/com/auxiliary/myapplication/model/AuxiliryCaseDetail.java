package com.auxiliary.myapplication.model;

public class AuxiliryCaseDetail {
    int icon;
    private String tag;
    private String tagDetail;


    public AuxiliryCaseDetail(int icon,String tag,String tagDetail){
        this.icon = icon;
        this.tag = tag;
        this.tagDetail = tagDetail;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagDetail() {
        return tagDetail;
    }

    public void setTagDetail(String tagDetail) {
        this.tagDetail = tagDetail;
    }
}
