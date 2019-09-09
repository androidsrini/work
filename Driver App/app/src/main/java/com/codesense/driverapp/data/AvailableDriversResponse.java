package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvailableDriversResponse{

	@SerializedName("available_drivers")
	private List<AvailableDriversItem> availableDrivers;

	@SerializedName("status")
	private int status;

	public void setAvailableDrivers(List<AvailableDriversItem> availableDrivers){
		this.availableDrivers = availableDrivers;
	}

	public List<AvailableDriversItem> getAvailableDrivers(){
		return availableDrivers;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"AvailableDriversResponse{" + 
			"available_drivers = '" + availableDrivers + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}