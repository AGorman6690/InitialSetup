package com.jobsearch.request

import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import java.util.List

import org.springframework.format.annotation.DateTimeFormat

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.maps.model.GeocodingResult
import com.jobsearch.category.service.Category
import com.jobsearch.google.GoogleClient

import  com.jobsearch.utilities.DateUtility

public class FindJobsRequest {
	int id

	int userId
	
	String address
//	String city
//	String state
//	String zipCode
	float lat
	float lng
	Integer radius
		

	String stringStartDate
	String stringEndDate
	String stringStartTime
	String stringEndTime
	LocalDate endDate
	LocalTime startTime
	LocalDate startDatel
	LocalTime endTime
	boolean beforeStartTime
	boolean beforeEndTime
	boolean beforeStartDate
	boolean beforeEndDate
	
	List<String> dates
	
	Double duration	
	boolean isShorterThanDuration
	
	int returnJobCount
	String sortBy
	boolean isAscending
	List<Integer> alreadyLoadedJobIds
	boolean isAppendingJobs
	boolean isSortingJobs


public FindJobFilterDTO(int radius, String fromAddress, int[] categoryIds, String startTime, String endTime,
			boolean beforeStartTime, boolean beforeEndTime, String startDate, String endDate, boolean beforeStartDate2,
			boolean beforeEndDate2, List<String> workingDays2, boolean doMatchAllDays, Double duration2, boolean lessThanDuration2,
			int returnJobCount, String sortBy, boolean isAscending, boolean isAppendingJobs2, 
			String city2, String state2, String zipCode2, String savedName) {
		// TODO Auto-generated constructor stub

			
		if(startTime != null) this.setStartTime_local(LocalTime.parse(startTime));
		if(endTime != null) this.setEndTime_local(LocalTime.parse(endTime));
		if(startDate != null) this.setStartDate_local(LocalDate.parse(startDate));
		if(endDate != null) this.setEndDate_local(LocalDate.parse(endDate));
		
		this.setSavedName(savedName);
		this.setCity(city2);
		this.setState(state2);
		this.setZipCode(zipCode2);
		this.setRadius(radius);

		this.setReturnJobCount(returnJobCount);

		this.setDuration(duration2);
		this.setIsShorterThanDuration(lessThanDuration2);

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
		this.doMatchAllDays
		
		
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
			
	public FindJobFilterDTO() {
		// TODO Auto-generated constructor stub
	}

}
