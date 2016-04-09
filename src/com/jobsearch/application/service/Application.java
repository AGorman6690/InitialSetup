package com.jobsearch.application.service;

import com.jobsearch.job.service.Job;
import com.jobsearch.user.service.JobSearchUser;

public class Application {
	private int applicationId;
	private int userId;
	private int jobId;
	private int beenViewed;
	private String jobName;
	private Job job;
	private JobSearchUser applicant;
	
	//Status values:
	//0: submitted
	//1: declined
	//2: considered
	//3: accepted
	private int status;

	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public JobSearchUser getApplicant() {
		return applicant;
	}
	public void setApplicant(JobSearchUser applicant) {
		this.applicant = applicant;
	}
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
	public int getBeenViewed() {
		return beenViewed;
	}
	public void setBeenViewed(int beenView) {
		this.beenViewed = beenView;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
