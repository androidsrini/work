package com.codesense.driverapp.ui.uploaddocument;

public class UploadDocumentModel {

    String status;
    String title;
    int image;

    public UploadDocumentModel(String status, String title, int image) {
        this.status = status;
        this.title = title;
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
