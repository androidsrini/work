package com.codesense.driverapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class CountriesItem implements Parcelable {

    @ColumnInfo(name = "country_dial_code")
    @SerializedName("country_dial_code")
    public String countryDialCode;

    @ColumnInfo(name = "country_name")
    @SerializedName("country_name")
    public String countryName;

    @PrimaryKey
    @ColumnInfo(name = "country_id")
    @SerializedName("country_id")
    @NonNull
    public String countryId;

    public String getCountryDialCode() {
        return countryDialCode;
    }

    public void setCountryDialCode(String countryDialCode) {
        this.countryDialCode = countryDialCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return countryName;
                /*"CountriesItem{" +
                        "country_dial_code = '" + countryDialCode + '\'' +
                        ",country_name = '" + countryName + '\'' +
                        ",country_id = '" + countryId + '\'' +
                        "}";*/
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryDialCode);
        dest.writeString(this.countryName);
        dest.writeString(this.countryId);
    }

    public CountriesItem() {
    }

    protected CountriesItem(Parcel in) {
        this.countryDialCode = in.readString();
        this.countryName = in.readString();
        this.countryId = in.readString();
    }

    public static final Creator<CountriesItem> CREATOR = new Creator<CountriesItem>() {
        @Override
        public CountriesItem createFromParcel(Parcel source) {
            return new CountriesItem(source);
        }

        @Override
        public CountriesItem[] newArray(int size) {
            return new CountriesItem[size];
        }
    };
}