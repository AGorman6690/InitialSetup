package com.jobsearch.model;

public class WageProposal {
	
	private int id;
//	private int jobId;
	private int applicationId;
	private double amount;
	private int proposedByUserId;
	private int proposedToUserId;
	
	
	//-1: No action taken;
	//0: Countered;
	//1: Accepted;
	//2: Declined;
	
	private int status;


	
	public int getApplicationId() {
		return applicationId;
	}


	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


//	public int getJobId() {
//		return jobId;
//	}
//
//
//	public void setJobId(int jobId) {
//		this.jobId = jobId;
//	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public int getProposedByUserId() {
		return proposedByUserId;
	}


	public void setProposedByUserId(int proposedByUserId) {
		this.proposedByUserId = proposedByUserId;
	}


	public int getProposedToUserId() {
		return proposedToUserId;
	}


	public void setProposedToUserId(int proposedToUserId) {
		this.proposedToUserId = proposedToUserId;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
