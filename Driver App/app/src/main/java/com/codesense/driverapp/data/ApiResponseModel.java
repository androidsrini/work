package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class ApiResponseModel {

	@SerializedName("public_key")
	private String publicKey;

	@SerializedName("app_name")
	private String appName;

	@SerializedName("version")
	private int version;

	@SerializedName("status")
	private int status;

	@SerializedName("updation_mandatory")
	private boolean updationMandatory;

	public void setPublicKey(String publicKey){
		this.publicKey = publicKey;
	}

	public String getPublicKey(){
		return publicKey;
	}

	public void setAppName(String appName){
		this.appName = appName;
	}

	public String getAppName(){
		return appName;
	}

	public void setVersion(int version){
		this.version = version;
	}

	public int getVersion(){
		return version;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setUpdationMandatory(boolean updationMandatory){
		this.updationMandatory = updationMandatory;
	}

	public boolean isUpdationMandatory(){
		return updationMandatory;
	}

	@Override
 	public String toString(){
		return 
			"ApiResponseModel{" +
			"public_key = '" + publicKey + '\'' + 
			",app_name = '" + appName + '\'' + 
			",version = '" + version + '\'' + 
			",status = '" + status + '\'' + 
			",updation_mandatory = '" + updationMandatory + '\'' + 
			"}";
		}
}