package com.codesense.driverapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VehiclesListItem implements Parcelable {

	@SerializedName("owner_as_driver")
	private String ownerAsDriver;

	@SerializedName("driver_id")
	private String driverId;

	@SerializedName("vehicle_type_id")
	private String vehicleTypeId;

	@SerializedName("driver_last_name")
	private String driverLastName;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("vehicle_type")
	private String vehicleType;

	@SerializedName("verification_status")
	private String verificationStatus;

	@SerializedName("vehicle_id")
	private String vehicleId;

	@SerializedName("driver_first_name")
	private String driverFirstName;

	@SerializedName("driver_email_id")
	private String driverEmailId;

	@SerializedName("vehicle_name")
	private String vehicleName;

	@SerializedName("duty_status")
	private String dutyStatus;

	public void setOwnerAsDriver(String ownerAsDriver){
		this.ownerAsDriver = ownerAsDriver;
	}

	public String getOwnerAsDriver(){
		return ownerAsDriver;
	}

	public void setDriverId(String driverId){
		this.driverId = driverId;
	}

	public String getDriverId(){
		return driverId;
	}

	public void setVehicleTypeId(String vehicleTypeId){
		this.vehicleTypeId = vehicleTypeId;
	}

	public String getVehicleTypeId(){
		return vehicleTypeId;
	}

	public void setDriverLastName(String driverLastName){
		this.driverLastName = driverLastName;
	}

	public String getDriverLastName(){
		return driverLastName;
	}

	public void setVehicleNumber(String vehicleNumber){
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public void setVehicleType(String vehicleType){
		this.vehicleType = vehicleType;
	}

	public String getVehicleType(){
		return vehicleType;
	}

	public void setVerificationStatus(String verificationStatus){
		this.verificationStatus = verificationStatus;
	}

	public String getVerificationStatus(){
		return verificationStatus;
	}

	public void setVehicleId(String vehicleId){
		this.vehicleId = vehicleId;
	}

	public String getVehicleId(){
		return vehicleId;
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

	public void setVehicleName(String vehicleName){
		this.vehicleName = vehicleName;
	}

	public String getVehicleName(){
		return vehicleName;
	}

	public void setDutyStatus(String dutyStatus){
		this.dutyStatus = dutyStatus;
	}

	public String getDutyStatus(){
		return dutyStatus;
	}

	@Override
 	public String toString(){
		return 
			"VehiclesListItem{" + 
			"owner_as_driver = '" + ownerAsDriver + '\'' + 
			",driver_id = '" + driverId + '\'' + 
			",vehicle_type_id = '" + vehicleTypeId + '\'' + 
			",driver_last_name = '" + driverLastName + '\'' + 
			",vehicle_number = '" + vehicleNumber + '\'' + 
			",vehicle_type = '" + vehicleType + '\'' + 
			",verification_status = '" + verificationStatus + '\'' + 
			",vehicle_id = '" + vehicleId + '\'' + 
			",driver_first_name = '" + driverFirstName + '\'' + 
			",driver_email_id = '" + driverEmailId + '\'' + 
			",vehicle_name = '" + vehicleName + '\'' + 
			",duty_status = '" + dutyStatus + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.ownerAsDriver);
		dest.writeString(this.driverId);
		dest.writeString(this.vehicleTypeId);
		dest.writeString(this.driverLastName);
		dest.writeString(this.vehicleNumber);
		dest.writeString(this.vehicleType);
		dest.writeString(this.verificationStatus);
		dest.writeString(this.vehicleId);
		dest.writeString(this.driverFirstName);
		dest.writeString(this.driverEmailId);
		dest.writeString(this.vehicleName);
		dest.writeString(this.dutyStatus);
	}

	public VehiclesListItem() {
	}

	protected VehiclesListItem(Parcel in) {
		this.ownerAsDriver = in.readString();
		this.driverId = in.readString();
		this.vehicleTypeId = in.readString();
		this.driverLastName = in.readString();
		this.vehicleNumber = in.readString();
		this.vehicleType = in.readString();
		this.verificationStatus = in.readString();
		this.vehicleId = in.readString();
		this.driverFirstName = in.readString();
		this.driverEmailId = in.readString();
		this.vehicleName = in.readString();
		this.dutyStatus = in.readString();
	}

	public static final Creator<VehiclesListItem> CREATOR = new Creator<VehiclesListItem>() {
		@Override
		public VehiclesListItem createFromParcel(Parcel source) {
			return new VehiclesListItem(source);
		}

		@Override
		public VehiclesListItem[] newArray(int size) {
			return new VehiclesListItem[size];
		}
	};
}