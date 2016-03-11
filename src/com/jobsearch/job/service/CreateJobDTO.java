package com.jobsearch.job.service;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateJobDTO {

	@JsonProperty("jobName")
	String jobName;

	@JsonProperty("userId")
	int userId;

	@JsonProperty("categoryId")
	int categoryId;

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
