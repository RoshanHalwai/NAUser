package com.kirtanlabs.nammaapartments;

import java.io.Serializable;

public class NammaApartmentUser implements Serializable {

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
    private String uid;
    private boolean verified;
    private boolean grantedAccess;
    private boolean admin;
    private String relation;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentUser() {
    }

    public NammaApartmentUser(String apartmentName, String emailId, String flatNumber, String fullName,
                              String phoneNumber, String societyName, String tenantType, String uid,
                              boolean verified, boolean grantedAccess, boolean admin, String relation) {
        this.apartmentName = apartmentName;
        this.emailId = emailId;
        this.flatNumber = flatNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.societyName = societyName;
        this.tenantType = tenantType;
        this.uid = uid;
        this.verified = verified;
        this.grantedAccess = grantedAccess;
        this.admin = admin;
        this.relation = relation;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public boolean isGrantedAccess() {
        return grantedAccess;
    }

    public boolean isAdmin() {
        return admin;
    }

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

    public String getUID() {
        return uid;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getRelation() {
        return relation;
    }
}
