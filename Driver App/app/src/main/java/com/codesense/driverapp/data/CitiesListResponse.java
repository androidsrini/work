package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitiesListResponse{

	@SerializedName("cities")
	private List<CitiesItem> cities;

	@SerializedName("status")
	private int status;

	public void setCities(List<CitiesItem> cities){
		this.cities = cities;
	}

	public List<CitiesItem> getCities(){
		return cities;
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
			"CitiesListResponse{" + 
			"cities = '" + cities + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}