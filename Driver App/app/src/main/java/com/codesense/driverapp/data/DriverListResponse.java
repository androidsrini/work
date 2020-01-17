package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DriverListResponse{

	@SerializedName("drivers_list")
	private List<DriversListItem> driversList;

	@SerializedName("status")
	private int status;

	public void setDriversList(List<DriversListItem> driversList){
		this.driversList = driversList;
	}

	public List<DriversListItem> getDriversList(){
		return driversList;
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
			"DriverListResponse{" + 
			"drivers_list = '" + driversList + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}