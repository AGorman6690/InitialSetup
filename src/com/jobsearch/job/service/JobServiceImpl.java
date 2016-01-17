package com.jobsearch.job.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.user.service.JobSearchUser;

@Service
public class JobServiceImpl {

	@Autowired
	JobRepository repository;

	public void addJob(String jobName, int userId) {
		repository.addJob(jobName, userId);
	}

	public ArrayList<Job> getJobs(JobSearchUser user) {

		return repository.getJobs(user);
	}

	public ArrayList<Job> getJobs(JobSearchUser user, boolean isActive) {
		return repository.getJobs(user, isActive);
	}

	public ArrayList<Job> getJobs(int userId, boolean isActive) {
		return repository.getJobs(userId, isActive);
	}

	public void updateJobComplete(int jobId) {
		repository.updateJobComplete(jobId);
	}

	public void applyForJob(int jobId, int userId) {
		repository.applyForJob(jobId, userId);
	}

	public ArrayList<Job> getJobsBySelectedCat(int categoryId) {
		return repository.getJobsBySelectedCat(categoryId);
	}

	public ArrayList<Job> getAppliedToJobs(JobSearchUser user, Boolean showOnlyActiveJobs) {
		return repository.getAppliedToJobs(user, showOnlyActiveJobs);
	}

	public ArrayList<Job> getJobsByCategory(int categoryId, boolean showOnlyActiveJobs) {
		return repository.getJobsByCategory(categoryId, showOnlyActiveJobs);
	}

	public ArrayList<Job> getEmployment(JobSearchUser user, boolean showOnlyActiveJobs) {
		return repository.getEmployment(user, showOnlyActiveJobs);
	}

	public void addJobCategory(int jobId, int categoryId) {
		repository.addJobCategory(jobId, categoryId);

	}

	public Job getJob(int jobId) {
		return repository.getJob(jobId);
	}
}
