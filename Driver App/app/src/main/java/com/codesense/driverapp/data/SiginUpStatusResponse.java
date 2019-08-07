package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class SiginUpStatusResponse{

	@SerializedName("signup_status")
	private SignupStatus signupStatus;

	@SerializedName("status")
	private int status;

	public void setSignupStatus(SignupStatus signupStatus){
		this.signupStatus = signupStatus;
	}

	public SignupStatus getSignupStatus(){
		return signupStatus;
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
			"SiginUpStatusResponse{" + 
			"signup_status = '" + signupStatus + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}