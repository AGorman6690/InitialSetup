package com.jobsearch.category.service;

import java.util.List;

import com.jobsearch.job.service.Job;
import com.jobsearch.user.service.JobSearchUser;

public class Category {
	
	private int id;
	private String name;
	private List<JobSearchUser> users;
	private List<Job> jobs;
	private List<Category> subCategories;
	
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

	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<Category> subCategories) {
		this.subCategories = subCategories;
	}
	

}
