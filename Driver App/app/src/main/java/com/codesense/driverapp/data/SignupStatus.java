package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class SignupStatus{

	@SerializedName("otp_verify")
	private String otpVerify;

	@SerializedName("agreement_accept")
	private String agreementAccept;

	@SerializedName("is_activated")
	private String isActivated;

	@SerializedName("owner_type_id")
	private String ownerTypeId;

	public void setOtpVerify(String otpVerify){
		this.otpVerify = otpVerify;
	}

	public String getOtpVerify(){
		return otpVerify;
	}

	public void setAgreementAccept(String agreementAccept){
		this.agreementAccept = agreementAccept;
	}

	public String getAgreementAccept(){
		return agreementAccept;
	}

	public void setIsActivated(String isActivated){
		this.isActivated = isActivated;
	}

	public String getIsActivated(){
		return isActivated;
	}

	public void setOwnerTypeId(String ownerTypeId){
		this.ownerTypeId = ownerTypeId;
	}

	public String getOwnerTypeId(){
		return ownerTypeId;
	}

	@Override
 	public String toString(){
		return 
			"SignupStatus{" + 
			"otp_verify = '" + otpVerify + '\'' + 
			",agreement_accept = '" + agreementAccept + '\'' + 
			",is_activated = '" + isActivated + '\'' + 
			",owner_type_id = '" + ownerTypeId + '\'' + 
			"}";
		}
}