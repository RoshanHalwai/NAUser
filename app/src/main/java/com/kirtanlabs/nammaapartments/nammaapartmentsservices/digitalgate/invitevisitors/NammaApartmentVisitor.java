package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/7/2018
 */

class NammaApartmentVisitor {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String UID;
    private String fullName;
    private String mobileNumber;
    private String dateAndTimeOfVisit;
    private String status;
    private String inviterUID;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentVisitor() {
    }

    public NammaApartmentVisitor(String UID, String fullName, String mobileNumber, String dateAndTimeOfVisit, String status, String inviterUID) {
        this.UID = UID;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.dateAndTimeOfVisit = dateAndTimeOfVisit;
        this.status = status;
        this.inviterUID = inviterUID;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getUID() {
        return UID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDateAndTimeOfVisit() {
        return dateAndTimeOfVisit;
    }

    public String getStatus() {
        return status;
    }

    public String getInviterUID() {
        return inviterUID;
    }
}