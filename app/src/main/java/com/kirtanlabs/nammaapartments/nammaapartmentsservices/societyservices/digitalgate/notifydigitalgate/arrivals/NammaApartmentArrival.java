package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.notifydigitalgate.arrivals;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 6/16/2018
 */

public class NammaApartmentArrival {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    /**
     * 'Reference' variable is a common variable used here to specify the cab number (in Expecting cab arrival) / vendor name (in expecting package arrival)
     */
    private String reference;
    private String dateAndTimeOfArrival;
    private String validFor;
    private String inviterUID;
    private String status;
    private String approvalType;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentArrival() {
    }

    NammaApartmentArrival(String reference, String dateAndTimeOfArrival, String validFor, String inviterUID, String approvalType) {
        this.reference = reference;
        this.dateAndTimeOfArrival = dateAndTimeOfArrival;
        this.validFor = validFor;
        this.inviterUID = inviterUID;
        this.status = com.kirtanlabs.nammaapartments.Constants.NOT_ENTERED;
        this.approvalType = approvalType;
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

    public String getInviterUID() {
        return inviterUID;
    }

    public String getStatus() {
        return status;
    }

    public String getApprovalType() {
        return approvalType;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setStatus(String status) {
        this.status = status;
    }
}
