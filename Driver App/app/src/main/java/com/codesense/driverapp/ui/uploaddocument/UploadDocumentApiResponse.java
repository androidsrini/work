package com.codesense.driverapp.ui.uploaddocument;

import com.codesense.driverapp.net.ApiResponse;

public class UploadDocumentApiResponse {

    private ApiResponse apiResponse;
    private ServiceType serviceType;

    private UploadDocumentApiResponse(ApiResponse apiResponse, ServiceType serviceType) {
        this.apiResponse = apiResponse;
        this.serviceType = serviceType;
    }

    public static UploadDocumentApiResponse newInstance(ApiResponse apiResponse, ServiceType serviceType) {
        return new UploadDocumentApiResponse(apiResponse, serviceType);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public enum ServiceType {
        DRIVER, VEHICLE_TYPES, VEHICLE
    }
}
