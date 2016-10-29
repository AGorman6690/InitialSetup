package com.jobsearch.model;

import com.jobsearch.job.service.Job;

public class FailedWageNegotiationDTO {
	WageProposal failedWageProposal;
	JobSearchUser otherUser;
	Job job;
	
	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public WageProposal getFailedWageProposal() {
		return failedWageProposal;
	}
	public void setFailedWageProposal(WageProposal failedWageProposal) {
		this.failedWageProposal = failedWageProposal;
	}
	public JobSearchUser getOtherUser() {
		return otherUser;
	}
	public void setOtherUser(JobSearchUser otherUser) {
		this.otherUser = otherUser;
	}
	
	
}
