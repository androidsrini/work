package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class DocumentStatus {

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("file")
    private String file;

    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("allow_update")
    private int allowUpdate;

    @SerializedName("status")
    private String status;

    public int getAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(int allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return
                "DocumentStatus{" +
                        "status_message = '" + statusMessage + '\'' +
                        ",file = '" + file + '\'' +
                        ",status_code = '" + statusCode + '\'' +
                        "}";
    }
}