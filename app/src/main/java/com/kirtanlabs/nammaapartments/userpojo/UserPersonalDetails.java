package com.kirtanlabs.nammaapartments.userpojo;

import java.io.Serializable;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 6/17/2018
 */
public class UserPersonalDetails implements Serializable{

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String email;
    private String fullName;
    private String phoneNumber;
    private String profilePhoto;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public UserPersonalDetails() {}

    public UserPersonalDetails(String email, String fullName, String phoneNumber, String profilePhoto) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
