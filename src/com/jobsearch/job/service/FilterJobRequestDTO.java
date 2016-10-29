package com.jobsearch.job.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.maps.model.GeocodingResult;
import com.jobsearch.category.service.Category;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.utilities.DateUtility;

public class FilterJobRequestDTO {

//	@JsonProperty("jobs")
//	List<Job> jobs;

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

	@JsonProperty("workingDays")
	List<String> workingDays;

	@JsonProperty("duration")
	double duration;

	@JsonProperty("lessThanDuration")
	boolean lessThanDuration;

	@JsonProperty("returnJobCount")
	int returnJobCount;	

	@JsonProperty("sortBy")
	String sortBy;
	
	@JsonProperty("isAscending")
	boolean isAscending;
	
	@JsonProperty("loadedJobIds")
	int[] loadedJobIds;
	
	@JsonProperty("isAppendingJobs")
	boolean isAppendingJobs;
	
	@JsonProperty("isSortingJobs")
	boolean isSortingJobs;	
	
	
	public boolean getIsSortingJobs() {
		return isSortingJobs;
	}

	public void setIsSortingJobs(boolean isSortingJobs) {
		this.isSortingJobs = isSortingJobs;
	}

	public boolean getIsAppendingJobs() {
		return isAppendingJobs;
	}

	public void setIsAppendingJobs(boolean isAppendingJobs) {
		this.isAppendingJobs = isAppendingJobs;
	}

	public int[] getLoadedJobIds() {
		return loadedJobIds;
	}

	public void setLoadedJobIds(int[] loadedJobIds) {
		this.loadedJobIds = loadedJobIds;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public boolean getIsAscending() {
		return isAscending;
	}

	public void setIsAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}	

	public int getReturnJobCount() {
		return returnJobCount;
	}

	public void setReturnJobCount(int returnJobCount) {
		this.returnJobCount = returnJobCount;
	}

	public boolean getLessThanDuration() {
		return lessThanDuration;
	}

	public void setLessThanDuration(boolean lessThanDuration) {
		this.lessThanDuration = lessThanDuration;
	}

	public List<String> getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(List<String> workingDays) {
		this.workingDays = workingDays;
	}

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

	public FilterJobRequestDTO(int radius, String fromAddress, int[] categoryIds, String startTime, String endTime,
			boolean beforeStartTime, boolean beforeEndTime, String startDate, String endDate, boolean beforeStartDate2,
			boolean beforeEndDate2, List<String> workingDays2, double duration2, boolean lessThanDuration2,
			int returnJobCount, String sortBy, boolean isAscending, boolean isAppendingJobs2) {
		// TODO Auto-generated constructor stub

		this.setRadius(radius);

		this.setReturnJobCount(returnJobCount);

		this.setDuration(duration2);
		this.setLessThanDuration(lessThanDuration2);

		this.setFromAddress(fromAddress);
		this.setCategoryIds(categoryIds);

		this.setStringStartTime(startTime);
		
		this.setStringEndTime(endTime);

		this.setStringStartDate(startDate);
		this.setStringEndDate(endDate);

		this.setBeforeEndTime(beforeEndTime);
		this.setBeforeStartTime(beforeStartTime);

		this.setBeforeStartDate(beforeStartDate2);
		this.setBeforeEndDate(beforeEndDate2);
		
		this.setSortBy(sortBy);
		this.setIsAscending(isAscending);
		
		this.setIsAppendingJobs(isAppendingJobs2);
		
//		this.setLoadedJobIds(loadedJobIds2);

		
		// Convert strings to sql Time objects.
		if(startTime != null){
			this.setStartTime(java.sql.Time.valueOf(startTime));
		}
		
		if(endTime != null){
			this.setEndTime(java.sql.Time.valueOf(endTime));
		}
		
		// Convert strings to sql Date objects
		this.setStartDate(DateUtility.getSqlDate(startDate, "yyyy-MM-dd"));
		this.setEndDate(DateUtility.getSqlDate(endDate, "yyyy-MM-dd"));

		this.setWorkingDays(workingDays2);
		
		
		
		//************************************************************
		//************************************************************
		//The user's requested locations should be cached somewhere with 
		//either cookies or a global array on the client side.
		//This would put less load on the Google API quota.
		//Or a table can be created that stores the lat/lng for all requested
		//city/states/zip code
		//************************************************************
		//************************************************************
		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(this.getFromAddress());

		// **********************************************************************
		// The below condition has been removed because "Woodbury, MN" returns
		// two results.
		// I left it as a reminder for whether we want to handle potential
		// ambiguous responses.
		// **********************************************************************
		// Filter location must return a valid response
		// if (results.length == 1){
		
		if(results.length > 0){
			this.setLng((float) results[0].geometry.location.lng);
			this.setLat((float) results[0].geometry.location.lat);	
		}
		

	}

	public FilterJobRequestDTO() {
		// TODO Auto-generated constructor stub
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

//	public List<Job> getJobs() {
//		return jobs;
//	}
//
//	public void setJobs(List<Job> jobs) {
//		this.jobs = jobs;
//	}

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
