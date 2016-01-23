package com.jobsearch.model;

import java.util.ArrayList;

import com.jobsearch.job.service.Job;

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
	
	private ArrayList<Job> jobs;

	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
}
