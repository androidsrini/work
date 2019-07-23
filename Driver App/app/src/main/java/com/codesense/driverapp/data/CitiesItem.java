package com.codesense.driverapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class CitiesItem implements Parcelable{

	@ColumnInfo(name = "city_name")
	@SerializedName("city_name")
	private String cityName;

	@ColumnInfo(name = "country_id")
	@SerializedName("country_id")
	private String countryId;

	@NonNull
	@PrimaryKey
	@ColumnInfo(name = "city_id")
	@SerializedName("city_id")
	private String cityId;

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setCountryId(String countryId){
		this.countryId = countryId;
	}

	public String getCountryId(){
		return countryId;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getCityId(){
		return cityId;
	}

	@Override
 	public String toString(){
		return cityName;
			/*"CitiesItem{" +
			"city_name = '" + cityName + '\'' + 
			",country_id = '" + countryId + '\'' + 
			",city_id = '" + cityId + '\'' + 
			"}";*/
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.cityName);
		dest.writeString(this.countryId);
		dest.writeString(this.cityId);
	}

	public CitiesItem() {
	}

	protected CitiesItem(Parcel in) {
		this.cityName = in.readString();
		this.countryId = in.readString();
		this.cityId = in.readString();
	}

	public static final Creator<CitiesItem> CREATOR = new Creator<CitiesItem>() {
		@Override
		public CitiesItem createFromParcel(Parcel source) {
			return new CitiesItem(source);
		}

		@Override
		public CitiesItem[] newArray(int size) {
			return new CitiesItem[size];
		}
	};
}