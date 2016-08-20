package com.jobsearch.job.service

import java.sql.Date
import java.sql.Time
import java.util.List

import org.codehaus.jackson.annotate.JsonProperty

import com.jobsearch.model.Question

class JobInfoPostRequestDTO {

	@JsonProperty("jobName")
	String jobName

	@JsonProperty("userId")
	int userId

	@JsonProperty("categoryIds")
	List<Integer> categoryIds

	@JsonProperty("openings")
	int openings

	@JsonProperty("description")
	String description

	@JsonProperty("streetAddress")
	String streetAddress

	@JsonProperty("city")
	String city

	@JsonProperty("state")
	String state

	@JsonProperty("zipCode")
	String zipCode

	@JsonProperty("lat")
	float lat

	@JsonProperty("lng")
	float lng

	@JsonProperty("startDate")
	Date startDate

	@JsonProperty("endDate")
	Date endDate

	@JsonProperty("stringStartDate")
	String stringStartDate

	@JsonProperty("stringEndDate")
	String stringEndDate

	@JsonProperty("startTime")
	Time startTime

	@JsonProperty("endTime")
	Time endTime

	@JsonProperty("stringStartTime")
	String stringStartTime

	@JsonProperty("stringEndTime")
	String stringEndTime

	@JsonProperty("selectedQuestionIds")
	List<Integer> selectedQuestionIds

	@JsonProperty("questions")
	List<Question> questions

}
