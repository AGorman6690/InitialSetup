package com.jobsearch.job.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.job.repository.JobRepository;

@Service
public class JobServiceImpl {

	@Autowired
	JobRepository repository;

	public void addJob(String jobName, int userId) {
		repository.addJob(jobName, userId);
	}

	public void updateJobComplete(int jobId) {
		repository.updateJobComplete(jobId);
	}

	public void applyForJob(int jobId, int userId) {
		repository.applyForJob(jobId, userId);
	}

	public List<Job> getJobsByCategory(int categoryId) {
		return repository.getJobsByCategory(categoryId);
	}
	
	public List<Job> getApplicationsByUser(int userId) {
		return repository.getApplicationsByUser(userId);
	}

	public List<Job> getEmploymentByUser(int userId) {
		return repository.getEmploymentByUser(userId);
	}

	public List<Job> getJobsByUser(int userId) {
		return repository.getJobsByUser(userId);
	}
}
