package com.hackathon.myntra_hackerramp.model;

import java.io.Serializable;

public class Vote implements Serializable {
    String key;
    String uid;

    public Vote(String key, String uid) {
        this.key = key;
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
