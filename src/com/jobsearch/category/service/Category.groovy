package com.jobsearch.category.service

import java.util.List;

import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;

public class Category {
	int id
	String name
	List<JobSearchUser> users
	List<Job> jobs
	List<Category> subCategories
	int subJobCount
	int jobCount
}
