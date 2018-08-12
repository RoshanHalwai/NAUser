package com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.pojo;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/13/2018
 */
public class Vehicle {

    private String addedDate;
    private String vehicleNumber;
    private String vehicleType;
    private String ownerName;

    public Vehicle() {
    }

    public Vehicle(String ownerName, String vehicleNumber, String vehicleType, String addedDate) {
        this.ownerName = ownerName;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.addedDate = addedDate;
    }

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
}
