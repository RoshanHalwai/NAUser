package com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/8/2018
 */

public class NammaApartmentDailyService implements Serializable {


    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String type;
    private String fullName;
    private String phoneNumber;
    private String profilePhoto;
    private String dateOfVisit;
    private String timeOfVisit;
    private int rating;
    private Long numberOfFlats;
    private String uid;
    private String dailyServiceHandedThingsDescription;
    private String status;
    private String userUID;
    private Map<String, String> handedThingsMap = new LinkedHashMap<>();

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDailyService() {
    }

    public NammaApartmentDailyService(NammaApartmentDailyService ds) {
        this(ds.uid, ds.fullName, ds.type, ds.phoneNumber, ds.profilePhoto, ds.timeOfVisit, ds.rating);
    }

    NammaApartmentDailyService(String uid, String fullName, String type, String phoneNumber, String profilePhoto, String timeOfVisit, int rating) {
        this.uid = uid;
        this.fullName = fullName;
        this.type = type;
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

    public void setTimeOfVisit(String timeOfVisit) {
        this.timeOfVisit = timeOfVisit;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserUID() {
        return userUID;
    }

    public Long getNumberOfFlats() {
        return numberOfFlats;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public Map<String, String> getHandedThingsMap() {
        return handedThingsMap;
    }

    public void setHandedThingsMap(Map<String, String> handedThingsMap) {
        this.handedThingsMap = handedThingsMap;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public void setNumberOfFlats(Long numberOfFlats) {
        this.numberOfFlats = numberOfFlats;
    }
}
