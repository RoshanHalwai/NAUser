package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.pojo;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 9/8/2018
 */
public class Transaction {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private float amount;
    private String paymentId;
    private String result;
    private String serviceCategory;
    private String userUID;
    private String uid;
    private long timestamp;
    private String period;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public Transaction() {
    }

    public Transaction(float amount, String paymentId, String result, String serviceCategory, String userUID, String uid, long timestamp) {
        this.amount = amount;
        this.paymentId = paymentId;
        this.result = result;
        this.serviceCategory = serviceCategory;
        this.userUID = userUID;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public float getAmount() {
        return amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getResult() {
        return result;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getUid() {
        return uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPeriod() {
        return period;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setPeriod(String period) {
        this.period = period;
    }
}
