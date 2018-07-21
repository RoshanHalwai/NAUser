package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.mydailyservices;

import java.io.Serializable;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/8/2018
 */

public class NammaApartmentDailyService implements Serializable {


    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String dailyServiceType;
    private String fullName;
    private String phoneNumber;
    private String profilePhoto;
    private String dateOfVisit;
    private String timeOfVisit;
    private int rating;
    private String uid;
    private String dailyServiceHandedThingsDescription;
    private String status;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDailyService() {
    }

    NammaApartmentDailyService(String uid, String fullName, String phoneNumber, String profilePhoto, String timeOfVisit, int rating) {
        this.uid = uid;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.timeOfVisit = timeOfVisit;
        this.rating = rating;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getTimeOfVisit() {
        return timeOfVisit;
    }

    public int getRating() {
        return rating;
    }

    public String getDailyServiceType() {
        return dailyServiceType;
    }

    public String getUID() {
        return uid;
    }

    public String getDailyServiceHandedThingsDescription() {
        return dailyServiceHandedThingsDescription;
    }

    public String getStatus() {
        return status;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }


    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTimeOfVisit(String timeOfVisit) {
        this.timeOfVisit = timeOfVisit;
    }

    public void setDailyServiceType(String dailyServiceType) {
        this.dailyServiceType = dailyServiceType;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setDailyServiceHandedThingsDescription(String dailyServiceHandedThingsDescription) {
        this.dailyServiceHandedThingsDescription = dailyServiceHandedThingsDescription;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
