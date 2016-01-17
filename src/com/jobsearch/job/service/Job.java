package com.jobsearch.job.service;

import java.util.ArrayList;

import com.jobsearch.category.service.Category;
import com.jobsearch.user.service.JobSearchUser;

public class Job {
	
	private int id;
	private int isActive;
	private int userId;
	private String jobName;
	private ArrayList<Category> categories;
	private ArrayList<JobSearchUser> employees;
	private ArrayList<JobSearchUser> applicants;
	
	public ArrayList<JobSearchUser> getApplicants() {
		return applicants;
	}

	public void setApplicants(ArrayList<JobSearchUser> applicants) {
		this.applicants = applicants;
	}

	public ArrayList<JobSearchUser> getEmployees() {
		return employees;
	}
	
	public void setEmployees(ArrayList<JobSearchUser> employees) {
		this.employees = employees;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public ArrayList<Category> getCategories() {
		return categories;
	}
	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
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
