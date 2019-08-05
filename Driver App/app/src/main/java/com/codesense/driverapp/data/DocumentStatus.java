package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class DocumentStatus{

	@SerializedName("status_message")
	private String statusMessage;

	@SerializedName("file")
	private String file;

	@SerializedName("status_code")
	private int statusCode;

	public void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage(){
		return statusMessage;
	}

	public void setFile(String file){
		this.file = file;
	}

	public String getFile(){
		return file;
	}

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	public int getStatusCode(){
		return statusCode;
	}

	@Override
 	public String toString(){
		return 
			"DocumentStatus{" + 
			"status_message = '" + statusMessage + '\'' + 
			",file = '" + file + '\'' + 
			",status_code = '" + statusCode + '\'' + 
			"}";
		}
}