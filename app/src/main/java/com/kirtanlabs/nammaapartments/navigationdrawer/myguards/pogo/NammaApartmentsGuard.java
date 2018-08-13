package com.kirtanlabs.nammaapartments.navigationdrawer.myguards.pogo;

public class NammaApartmentsGuard {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private String profilePhoto;
    private String status;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    NammaApartmentsGuard() {
    }

    public NammaApartmentsGuard(String fullName, String profilePhoto, String status) {
        this.fullName = fullName;
        this.profilePhoto = profilePhoto;
        this.status = status;
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
}
