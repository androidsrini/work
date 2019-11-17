package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class DocumentsItem {

    @SerializedName("name")
    private String name;

    @SerializedName("suported_formats")
    private List<String> suportedFormats;

    @SerializedName("is_mandatory")
    private int isMandatory;

    @SerializedName("max_size")
    private String maxSize;

    @SerializedName("field_name")
    private String fieldName;

    private DocumentStatus documentStatus;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String filePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public List<String> getSuportedFormats() {
        return suportedFormats;
    }

    public void setSuportedFormats(List<String> suportedFormats) {
        this.suportedFormats = suportedFormats;
    }

    public int getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(int isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void parseDocumentStatus(JSONObject jsonObject) {
        if (null != jsonObject) {
            JSONObject documentStatus = jsonObject.optJSONObject("document_status");
            if (null != documentStatus && !documentStatus.isNull(fieldName)) {
                JSONObject statusObject = documentStatus.optJSONObject(fieldName);
                DocumentStatus status = new DocumentStatus();
                status.setStatus(statusObject.optString("status"));
                status.setFile(statusObject.optString("file"));
                status.setStatusCode(statusObject.optInt("status_code"));
                status.setAllowUpdate(statusObject.optInt("allow_update", -1));
                this.documentStatus = status;
            }
        }
    }

    @Override
    public String toString() {
        return
                "DocumentsItem{" +
                        "name = '" + name + '\'' +
                        ",suported_formats = '" + suportedFormats + '\'' +
                        ",is_mandatory = '" + isMandatory + '\'' +
                        ",max_size = '" + maxSize + '\'' +
                        ",field_name = '" + fieldName + '\'' +
                        "}";
    }
}