package com.jobsearch.job.service;

import java.util.List;

import com.jobsearch.application.service.Application;
import com.jobsearch.category.service.Category;
import com.jobsearch.model.FailedWageNegotiationDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;

public class JobDTO {
	
	//**********************************************
	//**********************************************
	//The thought behind this DTO was to create a place to put all the
	//miscellaneous information that is sometimes associated with a job object.
	//The job class's property list is getting awfully long and confusing.
	//So any info that needs to be bundled with a job, for whatever purpose,
	//in order to display something to the user, I purpose putting the info here.
	//Then the job class can only contain the info related to a job (i.e. the columns in the job table).
	//**********************************************
	//**********************************************
	
	Job job;
	List<FailedWageNegotiationDTO> failedWageNegotiationDtos;
//	List<JobSearchUser> employees;
//	List<Category> categories;
//	List<Question> questions;
//	List<Application> applications;	
	
//	public List<JobSearchUser> getEmployees() {
//		return employees;
//	}
//
//	public void setEmployees(List<JobSearchUser> employees) {
//		this.employees = employees;
//	}
//
//	public List<Category> getCategories() {
//		return categories;
//	}
//
//	public void setCategories(List<Category> categories) {
//		this.categories = categories;
//	}
//
//	public List<Question> getQuestions() {
//		return questions;
//	}
//
//	public void setQuestions(List<Question> questions) {
//		this.questions = questions;
//	}
//
//	public List<Application> getApplications() {
//		return applications;
//	}
//
//	public void setApplications(List<Application> applications) {
//		this.applications = applications;
//	}
	
	public List<FailedWageNegotiationDTO> getFailedWageNegotiationDtos() {
		return failedWageNegotiationDtos;
	}

	public void setFailedWageNegotiationDtos(List<FailedWageNegotiationDTO> failedWageNegotiationDtos) {
		this.failedWageNegotiationDtos = failedWageNegotiationDtos;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	

}
