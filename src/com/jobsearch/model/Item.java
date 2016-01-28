package com.jobsearch.model;

import java.util.ArrayList;
import java.util.List;

import com.jobsearch.category.service.Category;
import com.jobsearch.job.service.Job;
import com.jobsearch.user.service.JobSearchUser;

public class Item {
//	private int userId;
//	private int categoryId;
//	private int jobId;
//	private String categoryName;
//	private String jobName;
//	private String email;
//	private String firstName;
//	private String lastName;
//	private int profileId;
	
	private List<Job> jobs;
	private List<Category> categories;
	private ArrayList<JobSearchUser> users;
	private List<RateCriterion> rateCriteria;
//	private ArrayList<JobSearchUser> employees;

	public List<Category> getCategories() {
		return categories;
	}

	public List<RateCriterion> getRateCriteria() {
		return rateCriteria;
	}

	public void setRateCriteria(List<RateCriterion> rateCriteria) {
		this.rateCriteria = rateCriteria;
	}

	public void setCategories(List<Category> list) {
		this.categories = list;
	}

	public ArrayList<JobSearchUser> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<JobSearchUser> applicants) {
		this.users = applicants;
	}

//	public ArrayList<JobSearchUser> getEmployees() {
//		return employees;
//	}
//
//	public void setEmployees(ArrayList<JobSearchUser> employees) {
//		this.employees = employees;
//	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
}
