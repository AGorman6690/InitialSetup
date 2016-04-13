package com.jobsearch.user.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditProfileDTO {
	
	@JsonProperty
	private int userId;
	
	@JsonProperty
	private List<Integer> categoryIds;
	
	@JsonProperty
	private float homeLat;
	
	@JsonProperty
	private float homeLng;
	
	@JsonProperty
	private String homeCity;
	
	@JsonProperty
	private String homeState;
	
	@JsonProperty
	private String homeZipCode;
	
	@JsonProperty
	private int maxWorkRadius;
	

	public int getMaxWorkRadius() {
		return maxWorkRadius;
	}

	public void setMaxWorkRadius(int maxWorkRadius) {
		this.maxWorkRadius = maxWorkRadius;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public String getHomeState() {
		return homeState;
	}

	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}

	public String getHomeZipCode() {
		return homeZipCode;
	}

	public void setHomeZipCode(String homeZipCode) {
		this.homeZipCode = homeZipCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public float getHomeLat() {
		return homeLat;
	}

	public void setHomeLat(float homeLat) {
		this.homeLat = homeLat;
	}

	public float getHomeLng() {
		return homeLng;
	}

	public void setHomeLng(float homeLng) {
		this.homeLng = homeLng;
	}
		
}
