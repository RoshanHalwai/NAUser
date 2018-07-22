package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

public class NammaApartmentSocietyServices {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String selectedProblem;
    private String timeSlot;
    private String uid;
    private String societyServiceType;
    private String notificationUID;
    private String status;
    private String takenBy;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentSocietyServices() {
    }

    NammaApartmentSocietyServices(String selectedProblem, String timeSlot, String uid, String societyServiceType,
                                  String notificationUID, String status, String takenBy) {
        this.selectedProblem = selectedProblem;
        this.timeSlot = timeSlot;
        this.uid = uid;
        this.societyServiceType = societyServiceType;
        this.notificationUID = notificationUID;
        this.status = status;
        this.takenBy = takenBy;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getSelectProblem() {
        return selectedProblem;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getUID() {
        return uid;
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

}
