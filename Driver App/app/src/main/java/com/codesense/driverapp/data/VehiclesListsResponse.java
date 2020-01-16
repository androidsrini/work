package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclesListsResponse{

	@SerializedName("vehicles_lists")
	private List<VehiclesListsItem> vehiclesLists;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setVehiclesLists(List<VehiclesListsItem> vehiclesLists){
		this.vehiclesLists = vehiclesLists;
	}

	public List<VehiclesListsItem> getVehiclesLists(){
		return vehiclesLists;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
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
			"VehiclesListsResponse{" + 
			"vehicles_lists = '" + vehiclesLists + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}