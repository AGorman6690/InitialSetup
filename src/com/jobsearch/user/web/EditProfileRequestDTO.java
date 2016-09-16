package com.jobsearch.user.web;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditProfileRequestDTO {
	
	@JsonProperty
	private int userId;
	
	@JsonProperty
	private List<Integer> categoryIds;
	
	@JsonProperty
	private Float homeLat;
	
	@JsonProperty
	private Float homeLng;
	
	@JsonProperty
	private String homeCity;
	
	@JsonProperty
	private String homeState;
	
	@JsonProperty
	private String homeZipCode;
	
	@JsonProperty
	private int maxWorkRadius;
	
	@JsonProperty
	private int minPay;

	
	public int getMinPay() {
		return minPay;
	}

	public void setMinPay(int minPay) {
		this.minPay = minPay;
	}

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

	public void setHomeLat(Float homeLat) {
		this.homeLat = homeLat;
	}

	public float getHomeLng() {
		return homeLng;
	}

	public void setHomeLng(Float homeLng) {
		this.homeLng = homeLng;
	}
		
}
