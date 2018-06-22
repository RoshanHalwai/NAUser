package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    private String timeOfVisit;
    private Boolean providedThings;
    private int rating;
    private String uid;
    private String dailyServiceHandedThingsDescription;
    private String status;
    private Map<String, Boolean> ownersUID;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDailyService() {
    }

    NammaApartmentDailyService(Map<String, Boolean> ownersUID, String uid, String fullName, String phoneNumber, String profilePhoto, String timeOfVisit, boolean providedThings, int rating) {
        this.uid = uid;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.timeOfVisit = timeOfVisit;
        this.providedThings = providedThings;
        this.rating = rating;
        this.status = status;
        this.ownersUID = ownersUID;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getfullName() {
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

    public Boolean getProvidedThings() {
        return providedThings;
    }

    public int getRating() {
        return rating;
    }

    public String getDailyServiceType() {
        return dailyServiceType;
    }

    public Map<String, Boolean> getOwnersUID() {
        return ownersUID;
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

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setDailyServiceType(String dailyServiceType) {
        this.dailyServiceType = dailyServiceType;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setDailyServiceHandedThingsDescription(String dailyServiceHandedThingsDescription) {
        this.dailyServiceHandedThingsDescription = dailyServiceHandedThingsDescription;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
