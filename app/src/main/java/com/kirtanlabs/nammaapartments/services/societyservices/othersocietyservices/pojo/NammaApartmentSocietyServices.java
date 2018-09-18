package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo;

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
    private Long timestamp;
    private String eventTitle;
    private String category;
    private String eventDate;
    private String quantity;
    private String scrapType;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentSocietyServices() {
    }

    public NammaApartmentSocietyServices(String problem, String timeSlot, String userUID, String societyServiceType,
                                         String notificationUID, String status, String endOTP, String takenBy) {
        this.problem = problem;
        this.timeSlot = timeSlot;
        this.userUID = userUID;
        this.societyServiceType = societyServiceType;
        this.notificationUID = notificationUID;
        this.status = status;
        this.endOTP = endOTP;
        this.takenBy = takenBy;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getCategory() {
        return category;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getScrapType() {
        return scrapType;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setScrapType(String scrapType) {
        this.scrapType = scrapType;
    }
}
