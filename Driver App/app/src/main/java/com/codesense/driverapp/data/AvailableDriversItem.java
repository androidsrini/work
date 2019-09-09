package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class AvailableDriversItem{

	@SerializedName("driver_id")
	private String driverId;

	@SerializedName("driver_last_name")
	private String driverLastName;

	@SerializedName("driver_first_name")
	private String driverFirstName;

	@SerializedName("driver_email_id")
	private String driverEmailId;

	public void setDriverId(String driverId){
		this.driverId = driverId;
	}

	public String getDriverId(){
		return driverId;
	}

	public void setDriverLastName(String driverLastName){
		this.driverLastName = driverLastName;
	}

	public String getDriverLastName(){
		return driverLastName;
	}

	public void setDriverFirstName(String driverFirstName){
		this.driverFirstName = driverFirstName;
	}

	public String getDriverFirstName(){
		return driverFirstName;
	}

	public void setDriverEmailId(String driverEmailId){
		this.driverEmailId = driverEmailId;
	}

	public String getDriverEmailId(){
		return driverEmailId;
	}

	@Override
 	public String toString(){
		return driverFirstName + " " + driverLastName;
		}
}