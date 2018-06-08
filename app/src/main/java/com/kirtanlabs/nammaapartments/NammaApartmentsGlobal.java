package com.kirtanlabs.nammaapartments;

import android.app.Application;

/**
 * Global NammaApartment class for storing User's Information
 */
public class NammaApartmentsGlobal extends Application {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private NammaApartmentUser nammaApartmentUser;

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public NammaApartmentUser getNammaApartmentUser() {
        return nammaApartmentUser;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setNammaApartmentUser(NammaApartmentUser nammaApartmentUser) {
        this.nammaApartmentUser = nammaApartmentUser;
    }

}
