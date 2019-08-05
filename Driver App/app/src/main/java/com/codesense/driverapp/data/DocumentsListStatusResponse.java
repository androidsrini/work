package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentsListStatusResponse{

	@SerializedName("documents_list")
	private List<DocumentsListItem> documentsList;

	@SerializedName("status")
	private int status;

	public void setDocumentsList(List<DocumentsListItem> documentsList){
		this.documentsList = documentsList;
	}

	public List<DocumentsListItem> getDocumentsList(){
		return documentsList;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"DocumentsListStatusResponse{" + 
			"documents_list = '" + documentsList + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}