package com.jobsearch.job.service;

import java.util.List;

import com.jobsearch.category.service.Category;
import com.jobsearch.user.service.JobSearchUser;

public class Job {
	
	private int id;
	private int isActive;
	private int userId;
	private String jobName;
	private List<Category> categories;
	private List<JobSearchUser> employees;
	private List<JobSearchUser> applicants;
	
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
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> list) {
		this.categories = list;
	}
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
