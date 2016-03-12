package com.jobsearch.job.service;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateJobDTO {

	@JsonProperty("jobName")
	String jobName;

	@JsonProperty("userId")
	int userId;

	@JsonProperty("categoryId")
	int categoryId;
	
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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
}
