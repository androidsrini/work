package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("email_id")
	private String emailId;

	@SerializedName("driver_id")
	private String driverId;

	@SerializedName("driver_last_name")
	private String driverLastName;

	@SerializedName("dial_code")
	private String dialCode;

	@SerializedName("allowed_vehicle")
	private String allowedVehicle;

	@SerializedName("driver_first_name")
	private String driverFirstName;

	@SerializedName("country_id")
	private String countryId;

	@SerializedName("contact_number")
	private String contactNumber;

	@SerializedName("status")
	private String status;

	public void setEmailId(String emailId){
		this.emailId = emailId;
	}

	public String getEmailId(){
		return emailId;
	}

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

	public void setDialCode(String dialCode){
		this.dialCode = dialCode;
	}

	public String getDialCode(){
		return dialCode;
	}

	public void setAllowedVehicle(String allowedVehicle){
		this.allowedVehicle = allowedVehicle;
	}

	public String getAllowedVehicle(){
		return allowedVehicle;
	}

	public void setDriverFirstName(String driverFirstName){
		this.driverFirstName = driverFirstName;
	}

	public String getDriverFirstName(){
		return driverFirstName;
	}

	public void setCountryId(String countryId){
		this.countryId = countryId;
	}

	public String getCountryId(){
		return countryId;
	}

	public void setContactNumber(String contactNumber){
		this.contactNumber = contactNumber;
	}

	public String getContactNumber(){
		return contactNumber;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"email_id = '" + emailId + '\'' + 
			",driver_id = '" + driverId + '\'' + 
			",driver_last_name = '" + driverLastName + '\'' + 
			",dial_code = '" + dialCode + '\'' + 
			",allowed_vehicle = '" + allowedVehicle + '\'' + 
			",driver_first_name = '" + driverFirstName + '\'' + 
			",country_id = '" + countryId + '\'' + 
			",contact_number = '" + contactNumber + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}