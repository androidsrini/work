package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleListResponse {

    @SerializedName("status")
    int status;
    @SerializedName("vehicles_list")
    private List<VehiclesListItem> vehiclesListItems;

    public List<VehiclesListItem> getVehiclesListItems() {
        return vehiclesListItems;
    }

    public void setVehiclesListItems(List<VehiclesListItem> vehiclesListItems) {
        this.vehiclesListItems = vehiclesListItems;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
