package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountriesListResponse{

	@SerializedName("countries")
	private List<CountriesItem> countries;

	@SerializedName("status")
	private int status;

	public void setCountries(List<CountriesItem> countries){
		this.countries = countries;
	}

	public List<CountriesItem> getCountries(){
		return countries;
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
			"CountriesListResponse{" + 
			"countries = '" + countries + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}