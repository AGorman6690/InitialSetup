package com.jobsearch.category.service;

import java.util.List;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.JobSearchUser;

public class Category {

	private int id;
	private String name;
	private List<JobSearchUser> users;
	private List<Job> jobs;
	private List<Category> subCategories;
	private int subJobCount;
	private int jobCount;
	// private List<Endorsement> endorsements;

	// public List<Endorsement> getEndorsements() {
	// return endorsements;
	// }
	//
	// public void setEndorsements(List<Endorsement> endorsements) {
	// this.endorsements = endorsements;
	// }

	public int getSubJobCount() {
		return subJobCount;
	}

	public void setSubJobCount(int subJobCount) {
		this.subJobCount = subJobCount;
	}

	public int getJobCount() {
		return jobCount;
	}

	public void setJobCount(int jobCount) {
		this.jobCount = jobCount;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<JobSearchUser> getUsers() {
		return users;
	}

	public void setUsers(List<JobSearchUser> users) {
		this.users = users;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<Category> subCategories) {
		this.subCategories = subCategories;
	}

}
