package com.codesense.driverapp.data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DocumentStatusResponse {

    @SerializedName("documents")
    private List<DocumentsItem> documents;

    @SerializedName("available_vehicles")
    private List<AvailableVehiclesItem> availableVehicles;

    @SerializedName("status")
    private int status;

    /*@SerializedName("vehicle_details")*/
    private List<VehicleDetails> vehicleDetails;
    private VehicleDetails vehicleDetailObject;

    public VehicleDetails getVehicleDetailObject() {
        return vehicleDetailObject;
    }

    public void setVehicleDetailObject(VehicleDetails vehicleDetailObject) {
        this.vehicleDetailObject = vehicleDetailObject;
    }

    public List<DocumentsItem> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentsItem> documents) {
        this.documents = documents;
    }

    public List<AvailableVehiclesItem> getAvailableVehicles() {
        return availableVehicles;
    }

    public void setAvailableVehicles(List<AvailableVehiclesItem> availableVehicles) {
        this.availableVehicles = availableVehicles;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<VehicleDetails> getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(List<VehicleDetails> vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public void parseDocumentStatus(JSONObject jsonObject) {
        if (null != jsonObject) {
            try {
                JSONObject vehicleDetailsObject = jsonObject.getJSONObject("vehicle_details");
                this.vehicleDetailObject = new Gson().fromJson(vehicleDetailsObject.toString(), VehicleDetails.class);
            } catch (JSONException e) {
                e.printStackTrace();
                JSONArray vehicleDetailsArray = jsonObject.optJSONArray("vehicle_details");
                this.vehicleDetails = new Gson().fromJson(vehicleDetailsArray.toString(), new TypeToken<List<VehicleDetails>>(){}.getType());
            }
        }
    }

    @Override
    public String toString() {
        return
                "DocumentStatusResponse{" +
                        "documents = '" + documents + '\'' +
                        ",available_vehicles = '" + availableVehicles + '\'' +
                        ",status = '" + status + '\'' +
                        ",vehicle_details = '" + vehicleDetails + '\'' +
                        "}";
    }
}