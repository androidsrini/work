package com.codesense.driverapp.data;

import java.io.Serializable;

public class AcceptResponseData implements Serializable {

    private int status;
    private String message;
    private TripDetailsDataRes tripDetailDataList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public TripDetailsDataRes getTripDetailDataList() {
        return tripDetailDataList;
    }

    public void setTripDetailDataList(TripDetailsDataRes tripDetailDataList) {
        this.tripDetailDataList = tripDetailDataList;
    }
}
