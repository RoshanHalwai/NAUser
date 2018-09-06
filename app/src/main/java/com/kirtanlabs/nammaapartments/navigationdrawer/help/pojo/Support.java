package com.kirtanlabs.nammaapartments.navigationdrawer.help.pojo;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 9/7/2018
 */
public class Support {

    private String problemDescription;
    private String serviceCategory;
    private String serviceType;
    private String status;
    private long timestamp;
    private String uid;
    private String userUID;

    public Support() {
    }

    public Support(String problemDescription, String serviceCategory, String serviceType, String status, long timestamp, String uid, String userUID) {
        this.problemDescription = problemDescription;
        this.serviceCategory = serviceCategory;
        this.serviceType = serviceType;
        this.status = status;
        this.timestamp = timestamp;
        this.uid = uid;
        this.userUID = userUID;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getStatus() {
        return status;
    }

    public String getUid() {
        return uid;
    }

    public String getUserUID() {
        return userUID;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
