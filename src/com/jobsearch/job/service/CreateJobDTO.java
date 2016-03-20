package com.jobsearch.job.service;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateJobDTO {

	@JsonProperty("jobName")
	String jobName;

	@JsonProperty("userId")
	int userId;

	@JsonProperty("categoryIds")
	List<Integer> categoryIds;

	@JsonProperty("openings")
	int openings;

	@JsonProperty("location")
	String location;

	@JsonProperty("description")
	String description;


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getOpenings() {
		return openings;
	}

	public void setOpenings(int openings) {
		this.openings = openings;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
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
}
