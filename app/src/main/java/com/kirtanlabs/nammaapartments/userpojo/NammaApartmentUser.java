package com.kirtanlabs.nammaapartments.userpojo;

import com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.pojo.Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NammaApartmentUser implements Serializable {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Map<String, Boolean> familyMembers = new HashMap<>();
    private final Map<String, Boolean> friends = new HashMap<>();
    private UserFlatDetails flatDetails;
    private UserPersonalDetails personalDetails;
    private UserPrivileges privileges;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentUser() {
    }

    public NammaApartmentUser(String uid, UserPersonalDetails personalDetails, UserFlatDetails flatDetails, UserPrivileges privileges) {
        this.uid = uid;
        this.personalDetails = personalDetails;
        this.flatDetails = flatDetails;
        this.privileges = privileges;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public UserFlatDetails getFlatDetails() {
        return flatDetails;
    }

    public UserPersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public UserPrivileges getPrivileges() {
        return privileges;
    }

    public String getUID() {
        return uid;
    }

    public Map<String, Boolean> getFamilyMembers() {
        return familyMembers;
    }

    public Map<String, Boolean> getFriends() {
        return friends;
    }

}
