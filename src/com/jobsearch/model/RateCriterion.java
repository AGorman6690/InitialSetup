package com.jobsearch.model;

public class RateCriterion {
	private int rateCriterionId;
	private String name;
	private int userId;
	private int jobId;
	private int value;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getRateCriterionId() {
		return rateCriterionId;
	}
	public void setRateCriterionId(int rateCriterionId) {
		this.rateCriterionId = rateCriterionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
