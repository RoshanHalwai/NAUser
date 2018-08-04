package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/22/2018
 */

public class NammaApartmentSocietyServices {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String problem;
    private String timeSlot;
    private String userUID;
    private String societyServiceType;
    private String notificationUID;
    private String status;
    private String takenBy;
    private String endOTP;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentSocietyServices() {
    }

    NammaApartmentSocietyServices(String problem, String timeSlot, String userUID, String societyServiceType,
                                  String notificationUID, String status, String endOTP) {
        this.problem = problem;
        this.timeSlot = timeSlot;
        this.userUID = userUID;
        this.societyServiceType = societyServiceType;
        this.notificationUID = notificationUID;
        this.status = status;
        this.endOTP = endOTP;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getProblem() {
        return problem;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getSocietyServiceType() {
        return societyServiceType;
    }

    public String getNotificationUID() {
        return notificationUID;
    }

    public String getStatus() {
        return status;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public String getEndOTP() {
        return endOTP;
    }

}
