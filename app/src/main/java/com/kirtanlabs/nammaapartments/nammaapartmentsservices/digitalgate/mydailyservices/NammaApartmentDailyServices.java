package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/8/2018
 */

class NammaApartmentDailyServices {


    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private String phoneNumber;
    private String profilePhoto;
    private Boolean providedThings;
    private int rating;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDailyServices() {
    }

    public NammaApartmentDailyServices(String fullName, String phoneNumber, String profilePhoto, boolean providedThings, int rating) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.providedThings = providedThings;
        this.rating = rating;
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

    public Boolean getProvidedThings() {
        return providedThings;
    }

    public int getRating() {
        return rating;
    }
}
