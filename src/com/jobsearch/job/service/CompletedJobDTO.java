package com.jobsearch.job.service;

import java.util.List;

import com.jobsearch.model.Endorsement;

public class CompletedJobDTO {
	
	private Job job;
	private String comment;
	private double rating;
	private List<Endorsement> endorsements;
	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	public List<Endorsement> getEndorsements() {
		return endorsements;
	}
	public void setEndorsements(List<Endorsement> endorsements) {
		this.endorsements = endorsements;
	}

}
