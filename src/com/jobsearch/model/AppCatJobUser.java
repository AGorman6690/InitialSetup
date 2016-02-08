package com.jobsearch.model;

public class AppCatJobUser {
	private String firstName;
	private String jobName;
	private int isOffered;
	private int beenViewed;
	private int isAccepted;
	private String categoryName;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
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
	public void setBeenViewed(int beenViewed) {
		this.beenViewed = beenViewed;
	}
	public int getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(int isAccepted) {
		this.isAccepted = isAccepted;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
