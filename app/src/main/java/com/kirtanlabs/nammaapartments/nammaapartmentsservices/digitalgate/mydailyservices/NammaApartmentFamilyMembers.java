package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import java.io.Serializable;

public class NammaApartmentFamilyMembers implements Serializable {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private String phoneNumber;
    private String relation;
    private Boolean grantedAccess;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentFamilyMembers() {
    }

    NammaApartmentFamilyMembers(String fullName, String phoneNumber, String relation, boolean grantedAccess) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.relation = relation;
        this.grantedAccess = grantedAccess;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getfullName() {
        return fullName;
    }

    public String getphoneNumber() {
        return phoneNumber;
    }

    public String getrelation() {
        return relation;
    }

    public Boolean getgrantedAccess() {
        return grantedAccess;
    }

}
