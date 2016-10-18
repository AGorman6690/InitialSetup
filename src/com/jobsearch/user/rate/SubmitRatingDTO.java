package com.jobsearch.user.rate;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobsearch.model.CommentDTO;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.RateCriterion;

public class SubmitRatingDTO {
	
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
	
	@JsonProperty("rateCriteria")
	List<RateCriterion> rateCriteria;
	
//	@JsonProperty("endorsements")
//	List<Endorsement> endorsements;
	
//	@JsonProperty("comment")
//	CommentDTO comment;
	
	List<Integer> endorsementCategoryIds;
	String commentString;
	
	
	public String getCommentString() {
		return commentString;
	}

	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}

	public List<Integer> getEndorsementCategoryIds() {
		return endorsementCategoryIds;
	}

	public void setEndorsementCategoryIds(List<Integer> endorsementCategoryIds) {
		this.endorsementCategoryIds = endorsementCategoryIds;
	}

//	public CommentDTO getComment() {
//		return comment;
//	}
//
//	public void setComment(CommentDTO comment) {
//		this.comment = comment;
//	}

//	public int getJobId() {
//		return jobId;
//	}
//
//	public void setJobId(int jobId) {
//		this.jobId = jobId;
//	}

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

//	public List<Endorsement> getEndorsements() {
//		return endorsements;
//	}
//
//	public void setEndorsements(List<Endorsement> endorsementCategoryIds) {
//		this.endorsements = endorsementCategoryIds;
//	}

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
