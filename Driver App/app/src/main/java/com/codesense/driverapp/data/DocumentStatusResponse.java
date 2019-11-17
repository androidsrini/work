package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentStatusResponse{

	@SerializedName("documents")
	private List<DocumentsItem> documents;

	@SerializedName("available_vehicles")
	private List<AvailableVehiclesItem> availableVehicles;

	@SerializedName("status")
	private int status;

	@SerializedName("vehicle_details")
	private VehicleDetails vehicleDetails;

	public void setDocuments(List<DocumentsItem> documents){
		this.documents = documents;
	}

	public List<DocumentsItem> getDocuments(){
		return documents;
	}

	public void setAvailableVehicles(List<AvailableVehiclesItem> availableVehicles){
		this.availableVehicles = availableVehicles;
	}

	public List<AvailableVehiclesItem> getAvailableVehicles(){
		return availableVehicles;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setVehicleDetails(VehicleDetails vehicleDetails){
		this.vehicleDetails = vehicleDetails;
	}

	public VehicleDetails getVehicleDetails(){
		return vehicleDetails;
	}

	@Override
 	public String toString(){
		return 
			"DocumentStatusResponse{" + 
			"documents = '" + documents + '\'' +
			",available_vehicles = '" + availableVehicles + '\'' + 
			",status = '" + status + '\'' + 
			",vehicle_details = '" + vehicleDetails + '\'' + 
			"}";
		}
}