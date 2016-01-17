package com.jobsearch.category.service;

import java.util.ArrayList;

import com.jobsearch.job.service.Job;
import com.jobsearch.user.service.JobSearchUser;

public class Category {
	
	private int id;
	private String name;
	private ArrayList<JobSearchUser> users;
	private ArrayList<Job> jobs;
	
	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}

	public ArrayList<JobSearchUser> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<JobSearchUser> users) {
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
	

}
