package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class VehiclesListsItem{

	@SerializedName("vehicle_type_id")
	private String vehicleTypeId;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("vehicle_id")
	private String vehicleId;

	@SerializedName("vehicle_name")
	private String vehicleName;

	public void setVehicleTypeId(String vehicleTypeId){
		this.vehicleTypeId = vehicleTypeId;
	}

	public String getVehicleTypeId(){
		return vehicleTypeId;
	}

	public void setVehicleNumber(String vehicleNumber){
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public void setVehicleId(String vehicleId){
		this.vehicleId = vehicleId;
	}

	public String getVehicleId(){
		return vehicleId;
	}

	public void setVehicleName(String vehicleName){
		this.vehicleName = vehicleName;
	}

	public String getVehicleName(){
		return vehicleName;
	}

	@Override
 	public String toString(){
		return 
			"VehiclesListsItem{" + 
			"vehicle_type_id = '" + vehicleTypeId + '\'' + 
			",vehicle_number = '" + vehicleNumber + '\'' + 
			",vehicle_id = '" + vehicleId + '\'' + 
			",vehicle_name = '" + vehicleName + '\'' + 
			"}";
		}
}