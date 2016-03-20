package com.jobsearch.job.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

@Service
public class JobServiceImpl {

	@Autowired
	JobRepository repository;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	ApplicationServiceImpl applicationService;

	@Autowired
	UserServiceImpl userService;

	public void addJob(List<CreateJobDTO> jobDtos) {
		repository.addJob(jobDtos);
	}


	public void markJobComplete(int jobId) {
		repository.markJobComplete(jobId);
	}

	public void applyForJob(int jobId, int userId) {

		if (!repository.hasAppliedForJob(jobId, userId)) {
			repository.applyForJob(jobId, userId);
		}

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


	public List<Job> getJobOffersByUser(int userId) {
		return repository.getJobOffersByUser(userId);
	}

	public List<Job> getActiveJobsByUser(int userId) {
		return repository.getActiveJobsByUser(userId);
	}

	public int getJobCountByCategory(int categoryId) {
		return repository.getJobCountByCategory(categoryId);
	}

	public int getSubJobCount(int categoryId, int count) {

		List<Category> categories;

		// For the categoryId passed as the paramenter, get the categories 1 level
		// deep
		categories = categoryService.getSubCategories(categoryId);

		// For each category 1 level deep
		for (Category category : categories) {

			// Get its job count
			count += getJobCountByCategory(category.getId());

			// Recursively get the job count for the category 1 level deep
			count = getSubJobCount(category.getId(), count);
		}

		return count;
	}

	public List<Job> getCompletedJobsByUser(int userId) {
		return repository.getCompletedJobsByUser(userId);
	}


	public Job getJob(int jobId) {
		// TODO Auto-generated method stub
		Job job = repository.getJob(jobId);

		// Get job applicants
		job.setApplicants(userService.getApplicants(jobId));

		// Set each applicant's rating
		for(JobSearchUser applicant : job.getApplicants()){
			applicant.setRatings(userService.getRatings(applicant.getUserId()));
			applicant.setApplication(applicationService.getApplication(jobId, applicant.getUserId()));
		}

		// Get job employees
		job.setEmployees(userService.getEmployeesByJob(jobId));

		// Set each employee's rating
		for(JobSearchUser employee : job.getEmployees()){
			employee.setRatings(userService.getRatings(employee.getUserId()));
		}

		return job;
	}

}
