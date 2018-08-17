package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo;

public class NammaApartmentEventManagement {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String eventTitle;
    private String category;
    private String userUID;
    private String societyServiceType;
    private String notificationUID;
    private String status;
    private String timeSlot;
    private String eventDate;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentEventManagement() {
    }

    public NammaApartmentEventManagement(String eventTitle, String category, String userUID, String societyServiceType,
                                         String notificationUID, String status, String timeSlot, String eventDate) {
        this.eventTitle = eventTitle;
        this.category = category;
        this.userUID = userUID;
        this.societyServiceType = societyServiceType;
        this.notificationUID = notificationUID;
        this.status = status;
        this.timeSlot = timeSlot;
        this.eventDate = eventDate;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getEventTitle() {
        return eventTitle;
    }

    public String getCategory() {
        return category;
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

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getEventDate() {
        return eventDate;
    }
}
