package com.codesense.driverapp.ui.uploaddocument;

import com.codesense.driverapp.net.ApiResponse;

public class UploadDocumentApiResponse {

    private ApiResponse apiResponse;
    /*private ServiceType serviceType;*/

    private UploadDocumentApiResponse(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

    public static UploadDocumentApiResponse newInstance(ApiResponse apiResponse) {
        return new UploadDocumentApiResponse(apiResponse);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    /*public ServiceType getServiceType() {
        return serviceType;
    }*/

    /*public enum ServiceType {
        DRIVER, VEHICLE_TYPES, VEHICLE, UPLOAD_DOCUEMNT, ALL_DOCUMENT, UPLOAD_DOCUEMNTS
    }*/
}
