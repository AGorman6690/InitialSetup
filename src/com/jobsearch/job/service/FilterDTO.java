package com.jobsearch.job.service;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.maps.model.GeocodingResult;
import com.jobsearch.category.service.Category;
import com.jobsearch.model.GoogleClient;
import com.jobsearch.utilities.DateUtility;

public class FilterDTO {

	@JsonProperty("jobs")
	List<Job> jobs;
	
	@JsonProperty("fromAddress")
	String fromAddress;
	
	@JsonProperty("lat")
	float lat;
	
	@JsonProperty("lng")
	float lng;
	
	@JsonProperty("radius")
	int radius;
	
	@JsonProperty("categoryIds")
	int[] categoryIds;
	
	@JsonProperty("categories")
	List<Category> categories;
	
	@JsonProperty("startDate")
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	Date startDate;
	
	@JsonProperty("endDate")
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	Date endDate;
	
	@JsonProperty("startTime")
	Time startTime;
	
	@JsonProperty("endTime")
	Time endTime;

	@JsonProperty("stringStartDate")
	String stringStartDate;
	
	@JsonProperty("stringEndDate")
	String stringEndDate;
				 
	@JsonProperty("stringStartTime")
	String stringStartTime;

	@JsonProperty("stringEndTime")
	String stringEndTime;
	
	@JsonProperty("beforeStartTime")
	boolean beforeStartTime;
	
	@JsonProperty("beforeEndTime")
	boolean beforeEndTime;
	
	@JsonProperty("beforeStartDate")
	boolean beforeStartDate;
	
	@JsonProperty("beforeEndDate")
	boolean beforeEndDate;
	
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public boolean getBeforeStartDate() {
		return beforeStartDate;
	}

	public void setBeforeStartDate(boolean beforeStartDate) {
		this.beforeStartDate = beforeStartDate;
	}

	public boolean getBeforeEndDate() {
		return beforeEndDate;
	}

	public void setBeforeEndDate(boolean beforeEndDate) {
		this.beforeEndDate = beforeEndDate;
	}

	@JsonProperty("duration")
	double duration;
	
	public String getStringStartDate() {
		return stringStartDate;
	}

	public void setStringStartDate(String stringStartDate) {
		this.stringStartDate = stringStartDate;
	}

	public String getStringEndDate() {
		return stringEndDate;
	}

	public void setStringEndDate(String stringEndDate) {
		this.stringEndDate = stringEndDate;
	}

	public boolean getBeforeStartTime() {
		return beforeStartTime;
	}

	public void setBeforeStartTime(boolean beforeStartTime) {
		this.beforeStartTime = beforeStartTime;
	}

	public boolean getBeforeEndTime() {
		return beforeEndTime;
	}

	public void setBeforeEndTime(boolean beforeEndTime) {
		this.beforeEndTime = beforeEndTime;
	}

	public static final String ZERO_TIME = "00:00:00";


	public FilterDTO(int radius, String fromAddress, int[] categoryIds, 
			String startTime, String endTime, boolean beforeStartTime, 
			boolean beforeEndTime, String startDate, String endDate, 
			boolean beforeStartDate2, boolean beforeEndDate2) {
		// TODO Auto-generated constructor stub
			
		this.setRadius(radius);
		
		this.setFromAddress(fromAddress);
		this.setCategoryIds(categoryIds);
		
		this.setStringStartTime(startTime);;
		this.setStringEndTime(endTime);
		
		this.setStringStartDate(startDate);
		this.setStringEndDate(endDate);
		
		this.setBeforeEndTime(beforeEndTime);
		this.setBeforeStartTime(beforeStartTime);
		
		this.setBeforeStartDate(beforeStartDate2);
		this.setBeforeEndDate(beforeEndDate2);
		
		//Convert strings to sql Time objects
		this.setStartTime(java.sql.Time.valueOf(startTime));
		this.setEndTime(java.sql.Time.valueOf(endTime));
		
		//Convert strings to sql Date objects
		this.setStartDate(DateUtility.getSqlDate(startDate, "MM/dd/yyyy"));
		this.setEndDate(DateUtility.getSqlDate(endDate, "MM/dd/yyyy"));

	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public String getStringStartTime() {
		return stringStartTime;
	}

	public void setStringStartTime(String stringStartTime) {
		this.stringStartTime = stringStartTime;
	}

	public String getStringEndTime() {
		return stringEndTime;
	}

	public void setStringEndTime(String stringEndTime) {
		this.stringEndTime = stringEndTime;
	}
	

	public int[] getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(int[] categoryIds) {
		this.categoryIds = categoryIds;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(GeocodingResult geocodingResults[]) {
		this.lat = (float) geocodingResults[0].geometry.location.lat;		
	}

	public float getLng() {
		return lng;
	}

	public void setLng(GeocodingResult[] geocodingResults) {
		this.lng = (float) geocodingResults[0].geometry.location.lng;
	}
	
	
	
	
}
