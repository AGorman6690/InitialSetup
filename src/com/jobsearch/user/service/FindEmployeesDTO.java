package com.jobsearch.user.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.maps.model.GeocodingResult;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.utilities.DateUtility;

public class FindEmployeesDTO {
	
	@JsonProperty
	private List<Date> availableDates;
	
	@JsonProperty
	private List<String> stringAvailableDates;
	
	@JsonProperty
	private String city;
	
	@JsonProperty
	private String state;
	
	@JsonProperty
	private String zipCode;
	
	@JsonProperty
	private Float lng;
	
	@JsonProperty
	private Float lat;
	
	@JsonProperty
	private int radius;
	
	@JsonProperty
	private List<Integer> categoryIds;
	
	
	
	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Float getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public FindEmployeesDTO(String city, String state, String zipCode, int radius, 
								List<String> dates, List<Integer> categoryIds2) {
		
		
		//Location parameters are verified on the client side.
		//At least one location parameter must be set.
		this.setCity(city);
		this.setState(state);
		this.setZipCode(zipCode);		
		
		//Radius is verified to be greater than 0 on the client side.
		this.setRadius(radius);
		
		if(categoryIds2.get(0) == -1){
			this.setCategoryIds(null);	
		}
		else{
			this.setCategoryIds(categoryIds2);
		}
		
		if(dates.get(0).matches("-1")){
			this.setStringAvailableDates(null);
			this.setAvailableDates(null);
		}else{
			this.setStringAvailableDates(dates);
						
			//Convert string dates to sql Dates
			this.availableDates = new ArrayList<Date>();
			for(String stringDate : dates){
				
				//The javascript date picker returns dates in the following format: "Wed Apr 13 2016 00:00:00 GMT-0500 (Central Daylight Time)"
				//Also, replace GMT with UTC. 
				//See this SO post for an explanation why to replace: http://stackoverflow.com/questions/9490373/java-unparseable-date-need-format-to-match-gmt-0400
				this.availableDates.add(DateUtility.getSqlDate(stringDate.replace("GMT-", "UTC-"), "EEE MMM dd yyyy HH:mm:ss zZ (zzzz)"));
			}
		}

		
		GoogleClient map = new GoogleClient();
		GeocodingResult results[] = map.getLatAndLng( city + " " + state + " " + zipCode );
		if(results.length == 1){
			this.setLat((float) results[0].geometry.location.lat);
			this.setLng((float) results[0].geometry.location.lng);
		}else{
			this.setLat(null);
			this.setLng(null);
		}
		
	}

	public List<String> getStringAvailableDates() {
		return stringAvailableDates;
	}

	public void setStringAvailableDates(List<String> stringAvailableDates) {
		this.stringAvailableDates = stringAvailableDates;
	}

	public List<Date> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(List<Date> availableDates) {
		this.availableDates = availableDates;
	}
	

}
