package com.jobsearch.model;

public class Application {
	private int applicationId;
	private int userId;
	private int jobId;
	private int isOffered;
	private int beenViewed;
	private int isAccepted;
	private String jobName;
	
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
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
	public int getIsOffered() {
		return isOffered;
	}
	public void setIsOffered(int isOffered) {
		this.isOffered = isOffered;
	}
	public int getBeenViewed() {
		return beenViewed;
	}
	public void setBeenViewed(int beenView) {
		this.beenViewed = beenView;
	}
	public int getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(int isAccepted) {
		this.isAccepted = isAccepted;
	}
	
}
