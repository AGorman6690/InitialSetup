package com.jobsearch.user.rate;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.RateCriterion;

public class RatingRequestDTO {
	
//	@JsonProperty("rateCriterionId")
//	int rateCriterionId;
////	
//	@JsonProperty("value")
//	double value;
//	
//	@JsonProperty("employeeId")
//	int employeeId;
//	
//	@JsonProperty("jobId")
//	int jobId;
	
	@JsonProperty("employeeId")
	int employeeId;
	
	@JsonProperty("jobId")
	int jobId;
	
	@JsonProperty("rateCriteria")
	List<RateCriterion> rateCriteria;
	
	@JsonProperty("endorsements")
	List<Endorsement> endorsements;
	
	@JsonProperty("comment")
	String comment;
	
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public List<RateCriterion> getRateCriteria() {
		return rateCriteria;
	}

	public void setRateCriteria(List<RateCriterion> rateCriteria) {
		this.rateCriteria = rateCriteria;
	}

	public List<Endorsement> getEndorsements() {
		return endorsements;
	}

	public void setEndorsements(List<Endorsement> endorsementCategoryIds) {
		this.endorsements = endorsementCategoryIds;
	}

//	public int getRateCriterionId() {
//		return rateCriterionId;
//	}
//
//	public void setRateCriterionId(int rateCriterionId) {
//		this.rateCriterionId = rateCriterionId;
//	}
//
//	public double getValue() {
//		return value;
//	}
//
//	public void setValue(double value) {
//		this.value = value;
//	}
//
//	public int getEmployeeId() {
//		return employeeId;
//	}
//
//	public void setEmployeeId(int employeeId) {
//		this.employeeId = employeeId;
//	}
//
//	public int getJobId() {
//		return jobId;
//	}
//
//	public void setJobId(int jobId) {
//		this.jobId = jobId;
//	}

}
