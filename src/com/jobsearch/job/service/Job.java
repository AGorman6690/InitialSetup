package com.jobsearch.job.service;

import java.util.List;

import com.jobsearch.application.service.Application;
import com.jobsearch.category.service.Category;
import com.jobsearch.user.service.JobSearchUser;

public class Job {
	
	private int id;
	private int isActive;
	private int userId;
	private String jobName;
	private Category category;
	private List<JobSearchUser> employees;
	private List<JobSearchUser> applicants;
	private List<Application> applications;
	
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public List<JobSearchUser> getApplicants() {
		return applicants;
	}

	public void setApplicants(List<JobSearchUser> list) {
		this.applicants = list;
	}

	public List<JobSearchUser> getEmployees() {
		return employees;
	}
	
	public void setEmployees(List<JobSearchUser> employees) {
		this.employees = employees;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
//	public List<Category> getCategories() {
//		return categories;
//	}
//	public void setCategories(List<Category> list) {
//		this.categories = list;
//	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
