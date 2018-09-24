package com.kirtanlabs.nammaapartments.navigationdrawer.myguards.pojo;

public class NammaApartmentsGuard {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private String profilePhoto;
    private String status;
    private int gateNumber;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    NammaApartmentsGuard() {
    }

    public NammaApartmentsGuard(String fullName, String profilePhoto, String status, int gateNumber) {
        this.fullName = fullName;
        this.profilePhoto = profilePhoto;
        this.status = status;
        this.gateNumber = gateNumber;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFullName() {
        return fullName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getStatus() {
        return status;
    }

    public int getGateNumber() {
        return gateNumber;
    }
}
