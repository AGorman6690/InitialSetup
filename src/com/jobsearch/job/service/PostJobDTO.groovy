package com.jobsearch.job.service

import java.util.List

import org.codehaus.jackson.annotate.JsonProperty

class PostJobDTO {
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

	@JsonProperty("selectedQuestionIds")
	List<Integer> selectedQuestionIds

	@JsonProperty("questions")
	List<PostQuestionDTO> questions

	@JsonProperty("workDays")
	List<WorkDay> workDays
}
