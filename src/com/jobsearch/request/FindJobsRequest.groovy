package com.jobsearch.request

import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

import com.google.maps.model.GeocodingResult
import com.jobsearch.google.GoogleClient
import com.jobsearch.utilities.DateUtility

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
	boolean isBeforeStartTime
	boolean isBeforeEndTime
	boolean isBeforeStartDate
	boolean isBeforeEndDate
	
	List<String> dates
	
	Double duration	
	boolean isShorterThanDuration
	
	int returnJobCount
	String sortBy
	boolean isAscending
	List<Integer> alreadyLoadedJobIds
	boolean isAppendingJobs
	boolean isSortingJobs

}
