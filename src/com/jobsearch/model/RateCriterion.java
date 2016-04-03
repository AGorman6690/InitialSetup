package com.jobsearch.model;

public class RateCriterion {
	private int rateCriterionId;
	private String name;
	private int employeeId;
	private int jobId;
	private double value;
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int userId) {
		this.employeeId = userId;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
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
