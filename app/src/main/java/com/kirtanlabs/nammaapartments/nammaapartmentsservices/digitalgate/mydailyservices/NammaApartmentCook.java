package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/8/2018
 */

class NammaApartmentCook {


    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String cookName;
    private String cookPhoneNumber;
    private String cookProfilePhoto;
    private Boolean providedThings;
    private int rating;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentCook() {
    }

    public NammaApartmentCook(String cookName, String cookPhoneNumber, String cookProfilePhoto, boolean providedThings, int rating) {
        this.cookName = cookName;
        this.cookPhoneNumber = cookPhoneNumber;
        this.cookProfilePhoto = cookProfilePhoto;
        this.providedThings = providedThings;
        this.rating = rating;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getCookName() {
        return cookName;
    }

    public String getCookPhoneNumber() {
        return cookPhoneNumber;
    }

    public String getCookProfilePhoto() {
        return cookProfilePhoto;
    }

    public Boolean getProvidedThings() {
        return providedThings;
    }

    public int getRating() {
        return rating;
    }
}
