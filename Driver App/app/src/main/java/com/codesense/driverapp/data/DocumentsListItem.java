package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class DocumentsListItem{

	@SerializedName("document_status")
	private DocumentStatus documentStatus;

	@SerializedName("name")
	private String name;

	@SerializedName("display_name")
	private String displayName;

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