package com.hackathon.myntra_hackerramp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Model implements Serializable {
    String uid;
    String username;
    String designUrl;
    String mlUrl;
    long upvotes;
    ArrayList<Vote> listOfUpvoters;
    Map timeStampmap;
    String timeStampStr;
    int voteStatus;
    ArrayList<Item> itemArrayList;
    String key;

    public Model() {
    }

    public Model(String uid, String username, String designUrl, String mlUrl, long upvotes, ArrayList<Vote> listOfUpvoters, Map timeStampmap, String timeStampStr, int voteStatus, ArrayList<Item> itemArrayList) {
        this.uid = uid;
        this.username = username;
        this.designUrl = designUrl;
        this.mlUrl = mlUrl;
        this.upvotes = upvotes;
        this.listOfUpvoters = listOfUpvoters;
        this.timeStampmap = timeStampmap;
        this.timeStampStr = timeStampStr;
        this.voteStatus = voteStatus;
        this.itemArrayList = itemArrayList;
    }

    public Model(String uid, String username, String designUrl, String mlUrl, long upvotes, ArrayList<Vote> listOfUpvoters, String timeStampStr, int voteStatus, ArrayList<Item> itemArrayList) {
        this.uid = uid;
        this.username = username;
        this.designUrl = designUrl;
        this.mlUrl = mlUrl;
        this.upvotes = upvotes;
        this.listOfUpvoters = listOfUpvoters;
        this.timeStampStr = timeStampStr;
        this.voteStatus = voteStatus;
        this.itemArrayList = itemArrayList;
    }

    public Model(String uid, String username, String designUrl, String mlUrl, long upvotes, ArrayList<Vote> listOfUpvoters, String timeStampStr, int voteStatus, ArrayList<Item> itemArrayList,String key) {
        this.uid = uid;
        this.username = username;
        this.designUrl = designUrl;
        this.mlUrl = mlUrl;
        this.upvotes = upvotes;
        this.listOfUpvoters = listOfUpvoters;
        this.timeStampStr = timeStampStr;
        this.voteStatus = voteStatus;
        this.itemArrayList = itemArrayList;
        this.key=key;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDesignUrl() {
        return designUrl;
    }

    public void setDesignUrl(String designUrl) {
        this.designUrl = designUrl;
    }

    public String getMlUrl() {
        return mlUrl;
    }

    public void setMlUrl(String mlUrl) {
        this.mlUrl = mlUrl;
    }

    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    public ArrayList<Vote> getListOfUpvoters() {
        return listOfUpvoters;
    }

    public void setListOfUpvoters(ArrayList<Vote> listOfUpvoters) {
        this.listOfUpvoters = listOfUpvoters;
    }

    public Map getTimeStampmap() {
        return timeStampmap;
    }

    public void setTimeStampmap(Map timeStampmap) {
        this.timeStampmap = timeStampmap;
    }

    public String getTimeStampStr() {
        return timeStampStr;
    }

    public void setTimeStampStr(String timeStampStr) {
        this.timeStampStr = timeStampStr;
    }

    public int getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(int voteStatus) {
        this.voteStatus = voteStatus;
    }

    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }
}
