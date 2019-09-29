package com.codesense.driverapp.ui.documentstatus;

import com.codesense.driverapp.net.ApiResponse;

public class DocumentStatusApiResponse {

    private ApiResponse apiResponse;
    private ServiceType serviceType;

    private DocumentStatusApiResponse(ApiResponse apiResponse, ServiceType serviceType) {
        this.apiResponse = apiResponse;
        this.serviceType = serviceType;
    }

    public static DocumentStatusApiResponse newInstance(ApiResponse apiResponse, ServiceType serviceType) {
        return new DocumentStatusApiResponse(apiResponse, serviceType);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public enum ServiceType {
        UPLOAD_DOCUEMNT, OWNER_CUM_DRIVER_STATUS, NON_DRIVING_PARTNER_STATUS
    }
}
