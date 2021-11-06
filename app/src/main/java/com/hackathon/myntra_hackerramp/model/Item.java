package com.hackathon.myntra_hackerramp.model;

public class Item {
    String itemId;
    String picUrl;
    long price;

    public Item(String itemId, String picUrl, long price) {
        this.itemId = itemId;
        this.picUrl = picUrl;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public long getPrice() {
        return price;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
