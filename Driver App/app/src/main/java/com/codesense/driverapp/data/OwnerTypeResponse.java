package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OwnerTypeResponse{

	@SerializedName("owner_types")
	private List<OwnerTypesItem> ownerTypes;

	@SerializedName("status")
	private int status;

	public void setOwnerTypes(List<OwnerTypesItem> ownerTypes){
		this.ownerTypes = ownerTypes;
	}

	public List<OwnerTypesItem> getOwnerTypes(){
		return ownerTypes;
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
			"OwnerTypeResponse{" + 
			"owner_types = '" + ownerTypes + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}