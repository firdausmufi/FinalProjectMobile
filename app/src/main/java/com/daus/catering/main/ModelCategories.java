package com.daus.catering.main;

import java.io.Serializable;


public class ModelCategories implements Serializable {

    int iIcon;
    String strName;

    public ModelCategories(int iIcon, String strName) {
        this.iIcon = iIcon;
        this.strName = strName;
    }

    public int getiIcon() {
        return iIcon;
    }

    public void setiIcon(int iIcon) {
        this.iIcon = iIcon;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }
}
