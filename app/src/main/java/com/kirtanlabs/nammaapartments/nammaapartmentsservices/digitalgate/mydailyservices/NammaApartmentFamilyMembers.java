package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import java.util.HashMap;
import java.util.Map;

class NammaApartmentFamilyMembers {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private String phoneNumber;
    private String profilePhoto;
    private String relation;
    private Boolean grantedAccess;
    private Map<String, Boolean> ownersUID = new HashMap<>();

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentFamilyMembers() {
    }

    NammaApartmentFamilyMembers(String fullName, String phoneNumber, String profilePhoto, String relation, boolean grantedAccess) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getrelation() {
        return relation;
    }

    public Boolean getgrantedAccess() {
        return grantedAccess;
    }

    public Map<String, Boolean> getOwnersUID() {
        return ownersUID;
    }

}
