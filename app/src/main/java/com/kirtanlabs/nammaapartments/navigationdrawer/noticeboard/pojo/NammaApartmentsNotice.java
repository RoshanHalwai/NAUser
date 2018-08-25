package com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.pojo;

public class NammaApartmentsNotice {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String nameOfAdmin;
    private String title;
    private String description;
    private String dateAndTime;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentsNotice() {

    }

    public NammaApartmentsNotice(String nameOfAdmin, String title, String description, String dateAndTime) {
        this.nameOfAdmin = nameOfAdmin;
        this.title = title;
        this.description = description;
        this.dateAndTime = dateAndTime;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getNameOfAdmin() {
        return nameOfAdmin;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

}

