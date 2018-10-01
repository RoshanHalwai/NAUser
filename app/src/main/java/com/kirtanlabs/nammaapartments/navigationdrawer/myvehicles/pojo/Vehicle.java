package com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.pojo;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/13/2018
 */
public class Vehicle {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String addedDate;
    private String vehicleNumber;
    private String vehicleType;
    private String ownerName;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public Vehicle() {
    }

    public Vehicle(String ownerName, String vehicleNumber, String vehicleType, String addedDate, String uid) {
        this.ownerName = ownerName;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.addedDate = addedDate;
        this.uid = uid;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getOwnerName() {
        return ownerName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getUid() {
        return uid;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
