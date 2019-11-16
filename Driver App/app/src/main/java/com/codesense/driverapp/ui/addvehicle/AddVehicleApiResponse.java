package com.codesense.driverapp.ui.addvehicle;

import com.codesense.driverapp.net.ApiResponse;

public class AddVehicleApiResponse {

    private ApiResponse apiResponse;
    /*private ServiceType serviceType;*/

    private AddVehicleApiResponse(ApiResponse apiResponse/*, ServiceType serviceType*/) {
        this.apiResponse = apiResponse;
        /*this.serviceType = serviceType;*/
    }

    public static AddVehicleApiResponse newInstance(ApiResponse apiResponse/*, ServiceType serviceType*/) {
        return new AddVehicleApiResponse(apiResponse/*, serviceType*/);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    /*public ServiceType getServiceType() {
        return serviceType;
    }

    public enum ServiceType {
        DRIVERS_LIST, VEHICLE_TYPES, COUNTRY_LIST, VEHICLE_TYPES_AND_DRIVERS_LIST,
        VEHICLE_TYPES_AND_DRIVERS_LIST_COUNTRY_LIST, ADD_DRIVER, ADD_VEHICLE
    }*/
}
