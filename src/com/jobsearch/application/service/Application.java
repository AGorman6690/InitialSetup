package com.jobsearch.application.service;

import java.util.List;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;

public class Application {
	private int applicationId;
	private int userId;
	private int jobId;
	private int hasBeenViewed;
	private String jobName;
	private Job job;
	private JobSearchUser applicant;
	private List<Question> questions;
	private List<WageProposal> wageProposals;
	private WageProposal currentWageProposal;
	//Status values:
	//0: submitted
	//1: declined
	//2: considered
	//3: accepted
	private int status;
	

	public WageProposal getCurrentWageProposal() {
		return currentWageProposal;
	}
	public void setCurrentWageProposal(WageProposal currentWageProposal) {
		this.currentWageProposal = currentWageProposal;
	}
	public List<WageProposal> getWageProposals() {
		return wageProposals;
	}
	public void setWageProposals(List<WageProposal> wageProposals) {
		this.wageProposals = wageProposals;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

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
	public int getHasBeenViewed() {
		return hasBeenViewed;
	}
	public void setHasBeenViewed(int hasBeenView) {
		this.hasBeenViewed = hasBeenView;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
