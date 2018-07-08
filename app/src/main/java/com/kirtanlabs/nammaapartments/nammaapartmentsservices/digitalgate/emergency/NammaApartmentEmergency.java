package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.emergency;

public class NammaApartmentEmergency {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private String emergencyType;
    private String fullName;
    private String phoneNumber;
    private String apartmentName;
    private String flatNumber;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentEmergency() {

    }

    NammaApartmentEmergency(String emergencyType, String fullName, String phoneNumber, String apartmentName, String flatNumber) {
        this.emergencyType = emergencyType;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.apartmentName = apartmentName;
        this.flatNumber = flatNumber;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getEmergencyType() {
        return emergencyType;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }
}
