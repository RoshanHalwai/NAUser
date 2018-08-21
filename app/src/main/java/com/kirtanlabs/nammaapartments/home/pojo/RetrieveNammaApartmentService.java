package com.kirtanlabs.nammaapartments.home.pojo;

public class RetrieveNammaApartmentService {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private int rating;
    private String timeSlot;
    private String noOfFlats;
    private String phoneNumber;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public RetrieveNammaApartmentService() {
    }

    RetrieveNammaApartmentService(String fullName, int rating, String timeSlot, String noOfFlats, String phoneNumber) {
        this.fullName = fullName;
        this.rating = rating;
        this.timeSlot = timeSlot;
        this.noOfFlats = noOfFlats;
        this.phoneNumber = phoneNumber;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFullName() {
        return fullName;
    }

    public int getRating() {
        return rating;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getNoOfFlats() {
        return noOfFlats;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
