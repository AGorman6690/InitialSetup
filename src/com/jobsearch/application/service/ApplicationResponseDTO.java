package com.jobsearch.application.service;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.WageProposal;

public class ApplicationResponseDTO {
	
	private Application application;
	private float currentDesiredWage;
	private float currentOfferedWage;
	private WageProposal currentWageProposal;
	private Job job;
	
	//***** NOTE ***** Currently a job is not being differentiated between not yet stated and started.
	//But I have a feeling this might be useful.
	//The "IsActive" column in the Job table should be "Status" and use the below values.
	//0 = not yet started;
	//1 = started;
	//2 = finished; 	
//	private int jobStatus;
	
	
	
	public WageProposal getCurrentWageProposal() {
		return currentWageProposal;
	}
	public float getCurrentOfferedWage() {
		return currentOfferedWage;
	}
	public void setCurrentOfferedWage(float currentOfferedWage) {
		this.currentOfferedWage = currentOfferedWage;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public void setCurrentWageProposal(WageProposal currentWageProposal) {
		this.currentWageProposal = currentWageProposal;
	}
//	public int getJobStatus() {
//		return jobStatus;
//	}
//	public void setJobStatus(int jobStatus) {
//		this.jobStatus = jobStatus;
//	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public float getCurrentDesiredWage() {
		return currentDesiredWage;
	}
	public void setCurrentDesiredWage(float currentDesiredWage) {
		this.currentDesiredWage = currentDesiredWage;
	}
	
	
}
