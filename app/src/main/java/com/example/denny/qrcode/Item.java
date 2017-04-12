package com.example.denny.qrcode;

/**
 * Created by denny on 4/9/17.
 */

public class Item {
    private String url;
    private String safety;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }



    public Item(String url, String safety) {
        this.url = url;
        this.safety = safety;
    }



}

