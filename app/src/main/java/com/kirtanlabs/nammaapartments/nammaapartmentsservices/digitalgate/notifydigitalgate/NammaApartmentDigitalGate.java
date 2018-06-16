package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/16/2018
 */

class NammaApartmentDigitalGate {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String reference;
    private String dateAndTimeOfArrival;
    private String validFor;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDigitalGate() {
    }

    NammaApartmentDigitalGate(String reference, String dateAndTimeOfArrival, String validFor, String uid) {
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
