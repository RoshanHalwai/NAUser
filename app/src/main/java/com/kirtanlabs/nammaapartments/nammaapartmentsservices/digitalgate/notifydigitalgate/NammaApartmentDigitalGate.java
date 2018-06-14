package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import java.util.HashMap;
import java.util.Map;

class NammaApartmentDigitalGate {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String reference;
    private String dateTimeOfArrival;
    private String validFor;
    private Map<String, Boolean> ownersUID = new HashMap<>();

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDigitalGate() {
    }

    NammaApartmentDigitalGate(String reference, String dateTimeOfArrival, String validFor) {
        this.reference = reference;
        this.dateTimeOfArrival = dateTimeOfArrival;
        this.validFor = validFor;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getReference() {
        return reference;
    }

    public String getDateTimeOfArrival() {
        return dateTimeOfArrival;
    }

    public String getValidFor() {
        return validFor;
    }

    public Map<String, Boolean> getOwnersUID() {
        return ownersUID;
    }

}
