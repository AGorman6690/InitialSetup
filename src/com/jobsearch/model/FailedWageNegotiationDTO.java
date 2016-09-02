package com.jobsearch.model;

public class FailedWageNegotiationDTO {
	WageProposal failedWageProposal;
	JobSearchUser otherUser;
	
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
