package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class OwnerTypesItem{

	@SerializedName("owner_type_id")
	private String ownerTypeId;

	@SerializedName("description")
	private String description;

	@SerializedName("owner_type")
	private String ownerType;

	public void setOwnerTypeId(String ownerTypeId){
		this.ownerTypeId = ownerTypeId;
	}

	public String getOwnerTypeId(){
		return ownerTypeId;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setOwnerType(String ownerType){
		this.ownerType = ownerType;
	}

	public String getOwnerType(){
		return ownerType;
	}

	@Override
 	public String toString(){
		return 
			"OwnerTypesItem{" + 
			"owner_type_id = '" + ownerTypeId + '\'' + 
			",description = '" + description + '\'' + 
			",owner_type = '" + ownerType + '\'' + 
			"}";
		}
}