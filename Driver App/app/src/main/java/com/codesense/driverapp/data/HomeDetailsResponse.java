package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class HomeDetailsResponse{

	@SerializedName("app_name")
	private String appName;

	@SerializedName("home_screen_image")
	private String homeScreenImage;

	@SerializedName("bottom_text")
	private String bottomText;

	@SerializedName("bottom_text_link")
	private String bottomTextLink;

	@SerializedName("status")
	private int status;

	public void setAppName(String appName){
		this.appName = appName;
	}

	public String getAppName(){
		return appName;
	}

	public void setHomeScreenImage(String homeScreenImage){
		this.homeScreenImage = homeScreenImage;
	}

	public String getHomeScreenImage(){
		return homeScreenImage;
	}

	public void setBottomText(String bottomText){
		this.bottomText = bottomText;
	}

	public String getBottomText(){
		return bottomText;
	}

	public void setBottomTextLink(String bottomTextLink){
		this.bottomTextLink = bottomTextLink;
	}

	public String getBottomTextLink(){
		return bottomTextLink;
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
			"HomeDetailsResponse{" + 
			"app_name = '" + appName + '\'' + 
			",home_screen_image = '" + homeScreenImage + '\'' + 
			",bottom_text = '" + bottomText + '\'' + 
			",bottom_text_link = '" + bottomTextLink + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}