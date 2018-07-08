package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.emergency;

import java.io.Serializable;

public class NammaApartmentEmergency implements Serializable {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private String emergencyType;
    private String fullName;
    private String phoneNumber;
    private String blockNumber;
    private String flatNumber;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentEmergency() {

    }

    NammaApartmentEmergency(String emergencyType, String fullName, String phoneNumber, String blockNumber, String flatNumber) {
        this.emergencyType = emergencyType;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.blockNumber = blockNumber;
        this.flatNumber = flatNumber;
    }
    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
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
