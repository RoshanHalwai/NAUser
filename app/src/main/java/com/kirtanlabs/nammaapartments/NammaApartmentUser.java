package com.kirtanlabs.nammaapartments;

public class NammaApartmentUser {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String apartmentName;
    private String emailId;
    private String flatNumber;
    private String fullName;
    private String phoneNumber;
    private String societyName;
    private String tenantType;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentUser() {
    }

    public NammaApartmentUser(String apartmentName, String emailId, String flatNumber, String fullName, String phoneNumber, String societyName, String tenantType) {
        this.apartmentName = apartmentName;
        this.emailId = emailId;
        this.flatNumber = flatNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.societyName = societyName;
        this.tenantType = tenantType;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getApartmentName() {
        return apartmentName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSocietyName() {
        return societyName;
    }

    public String getTenantType() {
        return tenantType;
    }

}
