package com.auxiliary.myapplication.model;

import java.util.List;

public class UpData {
    String caseInfo;
    List<String> images;

    public UpData(String caseInfo,List<String> images){
        this.caseInfo = caseInfo;
        this.images = images;
    }
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCaseInfo() {
        return caseInfo;
    }

    public void setCaseInfo(String caseInfo) {
        this.caseInfo = caseInfo;
    }
}
