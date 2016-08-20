package com.jobsearch.job.service

import java.sql.Date
import java.sql.Time
import java.util.List

import org.springframework.format.annotation.DateTimeFormat
import com.jobsearch.utilities.DateUtility

import com.fasterxml.jackson.annotation.JsonProperty
import com.jobsearch.category.service.Category

class FilterJobRequestDTO {

	@JsonProperty("fromAddress")
	String fromAddress

	@JsonProperty("lat")
	float lat

	@JsonProperty("lng")
	float lng

	@JsonProperty("radius")
	int radius

	@JsonProperty("categoryIds")
	int[] categoryIds

	@JsonProperty("categories")
	List<Category> categories

	@JsonProperty("startDate")
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	Date startDate

	@JsonProperty("endDate")
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	Date endDate

	@JsonProperty("startTime")
	Time startTime

	@JsonProperty("endTime")
	Time endTime

	@JsonProperty("stringStartDate")
	String stringStartDate

	@JsonProperty("stringEndDate")
	String stringEndDate

	@JsonProperty("stringStartTime")
	String stringStartTime

	@JsonProperty("stringEndTime")
	String stringEndTime

	@JsonProperty("beforeStartTime")
	boolean beforeStartTime

	@JsonProperty("beforeEndTime")
	boolean beforeEndTime

	@JsonProperty("beforeStartDate")
	boolean beforeStartDate

	@JsonProperty("beforeEndDate")
	boolean beforeEndDate

	@JsonProperty("workingDays")
	List<String> workingDays

	@JsonProperty("duration")
	double duration

	@JsonProperty("lessThanDuration")
	boolean lessThanDuration

	@JsonProperty("returnJobCount")
	int returnJobCount

	@JsonProperty("sortBy")
	String sortBy

	@JsonProperty("isAscending")
	boolean isAscending

	@JsonProperty("loadedJobIds")
	int[] loadedJobIds

	@JsonProperty("isAppendingJobs")
	boolean isAppendingJobs

	@JsonProperty("isSortingJobs")
	boolean isSortingJobs

	public static final String ZERO_TIME = "00:00:00"

	public FilterJobRequestDTO(int radius, String fromAddress, int[] categoryIds, String startTime, String endTime,
	boolean beforeStartTime, boolean beforeEndTime, String startDate, String endDate, boolean beforeStartDate,
	boolean beforeEndDate, List<String> workingDays, double duration, boolean lessThanDuration,
	int returnJobCount, String sortBy, boolean isAscending, boolean isAppendingJobs) {
		this.radius = radius
		this.returnJobCount = returnJobCount
		this.duration = duration
		this.lessThanDuration = lessThanDuration
		this.fromAddress = fromAddress
		this.categoryIds = categoryIds
		this.beforeEndTime = beforeEndTime
		this.beforeStartTime = beforeStartTime
		this.beforeStartDate = beforeStartDate
		this.beforeEndDate = beforeEndDate
		this.sortBy = sortBy
		this.isAscending = isAscending
		this.isAppendingJobs = isAppendingJobs
		this.workingDays = workingDays

		// Convert strings to sql Time objects.
		try {
			this.startTime = java.sql.Time.valueOf(startTime)
		} catch (Exception e) {
			//filter values were not sent from client
		}

		try {
			this.endTime = java.sql.Time.valueOf(endTime)
		} catch (Exception e) {
			//filter values were not sent from client
		}

		// Convert strings to sql Date objects
		this.startDate = DateUtility.getSqlDate(startDate, "MM/dd/yyyy")
		this.endDate = DateUtility.getSqlDate(endDate, "MM/dd/yyyy")

	}

	public FilterJobRequestDTO() {
		// TODO Auto-generated constructor stub
	}

}
