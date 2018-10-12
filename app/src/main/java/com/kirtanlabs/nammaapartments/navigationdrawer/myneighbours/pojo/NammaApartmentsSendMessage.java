package com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.pojo;

public class NammaApartmentsSendMessage {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String message;
    private String receiverUID;
    private Long timeStamp;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentsSendMessage() {
    }

    public NammaApartmentsSendMessage(String message, String receiverUID, Long timeStamp) {
        this.message = message;
        this.receiverUID = receiverUID;
        this.timeStamp = timeStamp;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getMessage() {
        return message;
    }

    public String getReceiverUID() {
        return receiverUID;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

}
