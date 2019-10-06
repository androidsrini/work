package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentsListStatusResponse {

    @SerializedName("documents_list")
    private List<DocumentsListItem> documentsList;

    @SerializedName("vehicle_details")
    private VehicleDetails vehicleDetails;
    @SerializedName("status")
    private int status;

    public VehicleDetails getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(VehicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public List<DocumentsListItem> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<DocumentsListItem> documentsList) {
        this.documentsList = documentsList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "DocumentsListStatusResponse{" +
                        "documents_list = '" + documentsList + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}