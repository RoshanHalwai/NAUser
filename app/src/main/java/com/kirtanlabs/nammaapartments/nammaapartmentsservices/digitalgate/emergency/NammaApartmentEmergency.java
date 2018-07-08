package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.emergency;

import java.io.Serializable;

public class NammaApartmentEmergency implements Serializable {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private String alarmType;
    private String fullName;
    private String phoneNumber;
    private String blockNumber;
    private String flatNumber;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentEmergency() {

    }

    NammaApartmentEmergency(String alarmType, String fullName, String phoneNumber, String blockNumber, String flatNumber) {
        this.alarmType = alarmType;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.blockNumber = blockNumber;
        this.flatNumber = flatNumber;
    }
    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }
}
