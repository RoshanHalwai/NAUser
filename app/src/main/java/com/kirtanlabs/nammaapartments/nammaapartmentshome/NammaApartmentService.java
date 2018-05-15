package com.kirtanlabs.nammaapartments.nammaapartmentshome;

public class NammaApartmentService {

    private final int serviceImage;
    private final String serviceName;

    public NammaApartmentService(int serviceImage, String serviceName) {
        this.serviceImage = serviceImage;
        this.serviceName = serviceName;
    }

    public int getServiceImage() {
        return serviceImage;
    }

    public String getServiceName() {
        return serviceName;
    }

}
