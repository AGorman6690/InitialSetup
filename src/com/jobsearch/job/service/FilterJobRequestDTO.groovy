package com.jobsearch.job.service

import java.sql.Date
import java.sql.Time
import java.util.List

import org.springframework.format.annotation.DateTimeFormat

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.maps.model.GeocodingResult
import com.jobsearch.category.service.Category
import com.jobsearch.google.GoogleClient

import  com.jobsearch.utilities.DateUtility

public class FilterJobRequestDTO {

	String fromAddress
	float lat
	float lng
	int radius
	int[] categoryIds
	List<Category> categories
	Date startDate
	Date endDate
	Time startTime
	Time endTime
	String stringStartDate
	String stringEndDate
	String stringStartTime
	String stringEndTime
	boolean beforeStartTime
	boolean beforeEndTime
	boolean beforeStartDate
	boolean beforeEndDate
	List<String> workingDays
	Double duration
	boolean isLessThanDuration
	int returnJobCount
	String sortBy
	boolean isAscending
	int[] loadedJobIds
	boolean isAppendingJobs
	boolean isSortingJobs
	Integer[] durationTypeIds

	public static final String ZERO_TIME = "00:00:00"

public FilterJobRequestDTO(int radius, String fromAddress, int[] categoryIds, String startTime, String endTime,
			boolean beforeStartTime, boolean beforeEndTime, String startDate, String endDate, boolean beforeStartDate2,
			boolean beforeEndDate2, List<String> workingDays2, Double duration2, boolean lessThanDuration2,
			int returnJobCount, String sortBy, boolean isAscending, boolean isAppendingJobs2, Integer[] durationTypeIds) {
		// TODO Auto-generated constructor stub

		this.setRadius(radius);
		
		this.setDurationTypeIds(durationTypeIds);

		this.setReturnJobCount(returnJobCount);

		this.setDuration(duration2);
		this.setIsLessThanDuration(lessThanDuration2);

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

}
