package com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices;

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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getTimeOfVisit() {
        return timeOfVisit;
    }

    public void setTimeOfVisit(String timeOfVisit) {
        this.timeOfVisit = timeOfVisit;
    }

    public int getRating() {
        return rating;
    }

    public String getDailyServiceType() {
        return dailyServiceType;
    }


    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setDailyServiceType(String dailyServiceType) {
        this.dailyServiceType = dailyServiceType;
    }

    public String getUID() {
        return uid;
    }

    public String getDailyServiceHandedThingsDescription() {
        return dailyServiceHandedThingsDescription;
    }

    public void setDailyServiceHandedThingsDescription(String dailyServiceHandedThingsDescription) {
        this.dailyServiceHandedThingsDescription = dailyServiceHandedThingsDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }
}
