package com.jobsearch.job.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.category.service.Category;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.model.CategoryJob;

@Service
public class JobServiceImpl {

	@Autowired
	JobRepository repository;

	public void addJob(String jobName, int userId) {
		repository.addJob(jobName, userId);
	}

	public void markJobComplete(int jobId) {
		repository.markJobComplete(jobId);
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

	public Job getJobByJobNameAndUser(String jobName, int userId) {
		return repository.getJobByJobNameAndUser(jobName, userId);
	}

	public List<Job> getJobOffersByUser(int userId) {
		return repository.getJobOffersByUser(userId);
	}

	public List<Job> getActiveJobsByUser_AppCat(int userId) {
		return repository.getActiveJobsByUser_AppCat(userId);
	}

	public int getJobCountByCategory(int categoryId) {
		return repository.getJobCountByCategory(categoryId);
	}

	public int getSubJobCount(int categoryId, int count) {
		
		return repository.getSubJobCount(categoryId, count);
	}

}
