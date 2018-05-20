package com.kirtanlabs.nammaapartments.nammaapartmentshome;

public class NammaApartmentService {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int serviceImage;
    private final String serviceName;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentService(int serviceImage, String serviceName) {
        this.serviceImage = serviceImage;
        this.serviceName = serviceName;
    }

    /* ------------------------------------------------------------- *
     * Public API (Getter & Setter)
     * ------------------------------------------------------------- */

    public int getServiceImage() {
        return serviceImage;
    }

    public String getServiceName() {
        return serviceName;
    }

}
