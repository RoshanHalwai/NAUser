package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.invitevisitors;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/7/2018
 */

public class NammaApartmentGuest {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String uid;
    private String fullName;
    private String mobileNumber;
    private String dateAndTimeOfVisit;
    private String status;
    private String inviterUID;
    private String profilePhoto;
    private String handedThings;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentGuest() {
    }

    public NammaApartmentGuest(String uid, String fullName, String mobileNumber, String dateAndTimeOfVisit, String inviterUID) {
        this.uid = uid;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.dateAndTimeOfVisit = dateAndTimeOfVisit;
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getHandedThings() {
        return handedThings;
    }

    public String getInviterUID() {
        return inviterUID;
    }

    public String getStatus() {
        return status;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateAndTimeOfVisit(String dateAndTimeOfVisit) {
        this.dateAndTimeOfVisit = dateAndTimeOfVisit;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setHandedThings(String handedThings) {
        this.handedThings = handedThings;
    }

}