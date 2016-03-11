package com.jobsearch.user.rate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RatingDTO {
	@JsonProperty("rateCriterionId")
	int rateCriterionId;
	@JsonProperty("value")
	double value;
	@JsonProperty("employeeId")
	int employeeId;
	@JsonProperty("jobId")
	int jobId;

	public int getRateCriterionId() {
		return rateCriterionId;
	}

	public void setRateCriterionId(int rateCriterionId) {
		this.rateCriterionId = rateCriterionId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

}
