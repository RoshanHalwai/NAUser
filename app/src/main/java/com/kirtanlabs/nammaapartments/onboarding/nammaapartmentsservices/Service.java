package com.kirtanlabs.nammaapartments.onboarding.nammaapartmentsservices;

public class Service {

    private int serviceImage;
    private String serviceName;

    public Service(int serviceImage, String serviceName) {
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
