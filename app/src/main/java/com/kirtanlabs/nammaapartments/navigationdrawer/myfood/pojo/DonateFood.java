package com.kirtanlabs.nammaapartments.navigationdrawer.myfood.pojo;

public class DonateFood {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String foodType;
    private String foodQuantity;
    private String uid;
    private String userUID;
    private long timestamp;
    private String status;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public DonateFood() {

    }

    public DonateFood(String foodType, String foodQuantity, String uid, String userUID, long timestamp, String status) {
        this.foodType = foodType;
        this.foodQuantity = foodQuantity;
        this.uid = uid;
        this.userUID = userUID;
        this.timestamp = timestamp;
        this.status = status;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFoodType() {
        return foodType;
    }

    public String getFoodQuantity() {
        return foodQuantity;
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

    public String getStatus() {
        return status;
    }
}
