package com.kirtanlabs.nammaapartments.onboarding.nammaapartmentsservices;

class Service {

    private final int serviceImage;
    private final String serviceName;

    Service(int serviceImage, String serviceName) {
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
