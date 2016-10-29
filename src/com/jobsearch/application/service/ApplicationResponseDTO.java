package com.jobsearch.application.service;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.WageProposal;

public class ApplicationResponseDTO {

	//**********************************************
	//**********************************************
	//It now appears that this DTO can be done away with.
	//It has basically became the Application.java class with one or two properties missing.
	//There is no reason the Application class cannot have those properties.
	//Consider removing this DTO class entirely
	//**********************************************
	//**********************************************

	private Application application;
	private float currentDesiredWage;
	private float currentOfferedWage;
	private WageProposal currentWageProposal;
	private Job job;



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
