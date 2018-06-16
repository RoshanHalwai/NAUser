package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/16/2018
 */

class NammaApartmentArrival {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    /**
     * 'Reference' variable is a common variable used here to specify the cab number (in Expecting cab arrival) / vendor name (in expecting package arrival)
     */
    private String reference;
    private String dateAndTimeOfArrival;
    private String validFor;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentArrival() {
    }

    NammaApartmentArrival(String reference, String dateAndTimeOfArrival, String validFor, String uid) {
        this.reference = reference;
        this.dateAndTimeOfArrival = dateAndTimeOfArrival;
        this.validFor = validFor;
        this.uid = uid;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getReference() {
        return reference;
    }

    public String getDateAndTimeOfArrival() {
        return dateAndTimeOfArrival;
    }

    public String getValidFor() {
        return validFor;
    }

    public String getUid() {
        return uid;
    }
}
