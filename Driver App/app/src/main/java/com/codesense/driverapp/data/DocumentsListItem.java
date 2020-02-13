package com.codesense.driverapp.data;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class DocumentsListItem{

	@SerializedName("document_status")
	private DocumentStatus documentStatus;

	@SerializedName("name")
	private String name;

	@SerializedName("display_name")
	private String displayName;

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private Uri selectedFileUri;

	public boolean isImageFile() {
		return isImageFile;
	}

	private boolean isImageFile;
	private String fileName;

	public Uri getSelectedFileUri() {
		return selectedFileUri;
	}

	public void setImageFile(boolean imageFile) {
		isImageFile = imageFile;
	}

	public void setSelectedFileUri(Uri selectedFileUri) {
		this.selectedFileUri = selectedFileUri;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setDocumentStatus(DocumentStatus documentStatus){
		this.documentStatus = documentStatus;
	}

	public DocumentStatus getDocumentStatus(){
		return documentStatus;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	@Override
 	public String toString(){
		return 
			"DocumentsListItem{" + 
			"document_status = '" + documentStatus + '\'' + 
			",name = '" + name + '\'' + 
			",display_name = '" + displayName + '\'' + 
			"}";
		}
}