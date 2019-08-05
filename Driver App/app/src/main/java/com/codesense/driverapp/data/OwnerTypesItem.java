package com.codesense.driverapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OwnerTypesItem implements Parcelable{

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.ownerTypeId);
		dest.writeString(this.description);
		dest.writeString(this.ownerType);
	}

	public OwnerTypesItem() {
	}

	protected OwnerTypesItem(Parcel in) {
		this.ownerTypeId = in.readString();
		this.description = in.readString();
		this.ownerType = in.readString();
	}

	public static final Creator<OwnerTypesItem> CREATOR = new Creator<OwnerTypesItem>() {
		@Override
		public OwnerTypesItem createFromParcel(Parcel source) {
			return new OwnerTypesItem(source);
		}

		@Override
		public OwnerTypesItem[] newArray(int size) {
			return new OwnerTypesItem[size];
		}
	};
}