package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/7/2018
 */

public class NammaApartmentVisitor {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String uid;
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

    NammaApartmentVisitor(String uid, String fullName, String mobileNumber, String dateAndTimeOfVisit, String status, String inviterUID) {
        this.uid = uid;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.dateAndTimeOfVisit = dateAndTimeOfVisit;
        this.status = status;
        this.inviterUID = inviterUID;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getUid() {
        return uid;
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