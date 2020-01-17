package com.codesense.driverapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DriversListItem implements Parcelable {

	@SerializedName("driver_contact_number")
	private String driverContactNumber;

	@SerializedName("driver_id")
	private String driverId;

	@SerializedName("driver_last_name")
	private String driverLastName;

	@SerializedName("driver_country_dial_code")
	private String driverCountryDialCode;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("invite_code")
	private String inviteCode;

	@SerializedName("verification_status")
	private String verificationStatus;

	@SerializedName("driver_first_name")
	private String driverFirstName;

	@SerializedName("vehicle_id")
	private String vehicleId;

	@SerializedName("driver_email_id")
	private String driverEmailId;

	@SerializedName("driving_activation_status")
	private String drivingActivationStatus;

	public void setDriverContactNumber(String driverContactNumber){
		this.driverContactNumber = driverContactNumber;
	}

	public String getDriverContactNumber(){
		return driverContactNumber;
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

	public void setDriverCountryDialCode(String driverCountryDialCode){
		this.driverCountryDialCode = driverCountryDialCode;
	}

	public String getDriverCountryDialCode(){
		return driverCountryDialCode;
	}

	public void setVehicleNumber(String vehicleNumber){
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public void setInviteCode(String inviteCode){
		this.inviteCode = inviteCode;
	}

	public String getInviteCode(){
		return inviteCode;
	}

	public void setVerificationStatus(String verificationStatus){
		this.verificationStatus = verificationStatus;
	}

	public String getVerificationStatus(){
		return verificationStatus;
	}

	public void setDriverFirstName(String driverFirstName){
		this.driverFirstName = driverFirstName;
	}

	public String getDriverFirstName(){
		return driverFirstName;
	}

	public void setVehicleId(String vehicleId){
		this.vehicleId = vehicleId;
	}

	public String getVehicleId(){
		return vehicleId;
	}

	public void setDriverEmailId(String driverEmailId){
		this.driverEmailId = driverEmailId;
	}

	public String getDriverEmailId(){
		return driverEmailId;
	}

	public void setDrivingActivationStatus(String drivingActivationStatus){
		this.drivingActivationStatus = drivingActivationStatus;
	}

	public String getDrivingActivationStatus(){
		return drivingActivationStatus;
	}

	@Override
 	public String toString(){
		return 
			"DriversListItem{" + 
			"driver_contact_number = '" + driverContactNumber + '\'' + 
			",driver_id = '" + driverId + '\'' + 
			",driver_last_name = '" + driverLastName + '\'' + 
			",driver_country_dial_code = '" + driverCountryDialCode + '\'' + 
			",vehicle_number = '" + vehicleNumber + '\'' + 
			",invite_code = '" + inviteCode + '\'' + 
			",verification_status = '" + verificationStatus + '\'' + 
			",driver_first_name = '" + driverFirstName + '\'' + 
			",vehicle_id = '" + vehicleId + '\'' + 
			",driver_email_id = '" + driverEmailId + '\'' + 
			",driving_activation_status = '" + drivingActivationStatus + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.driverContactNumber);
		dest.writeString(this.driverId);
		dest.writeString(this.driverLastName);
		dest.writeString(this.driverCountryDialCode);
		dest.writeString(this.vehicleNumber);
		dest.writeString(this.inviteCode);
		dest.writeString(this.verificationStatus);
		dest.writeString(this.driverFirstName);
		dest.writeString(this.vehicleId);
		dest.writeString(this.driverEmailId);
		dest.writeString(this.drivingActivationStatus);
	}

	public DriversListItem() {
	}

	protected DriversListItem(Parcel in) {
		this.driverContactNumber = in.readString();
		this.driverId = in.readString();
		this.driverLastName = in.readString();
		this.driverCountryDialCode = in.readString();
		this.vehicleNumber = in.readString();
		this.inviteCode = in.readString();
		this.verificationStatus = in.readString();
		this.driverFirstName = in.readString();
		this.vehicleId = in.readString();
		this.driverEmailId = in.readString();
		this.drivingActivationStatus = in.readString();
	}

	public static final Creator<DriversListItem> CREATOR = new Creator<DriversListItem>() {
		@Override
		public DriversListItem createFromParcel(Parcel source) {
			return new DriversListItem(source);
		}

		@Override
		public DriversListItem[] newArray(int size) {
			return new DriversListItem[size];
		}
	};
}