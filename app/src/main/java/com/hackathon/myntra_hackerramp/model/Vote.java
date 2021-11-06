package com.hackathon.myntra_hackerramp.model;

public class Vote {
    String itemId;
    String uid;

    public Vote(String itemId, String uid) {
        this.itemId = itemId;
        this.uid = uid;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
