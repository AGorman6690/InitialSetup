package com.jobsearch.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.repository.ApplicationRepository;

@Service
public class ApplicationServiceImpl {
	
	@Autowired
	ApplicationRepository repository;
	

	public void markApplicationViewed(int jobId, int userId) {
		repository.markApplicationViewed(jobId, userId);		
	}
	

	public void markApplicationAccepted(int jobId, int userId) {
		repository.markApplicationAccepted(jobId, userId);
		
	}
	

	public List<Application> getApplicationsByEmployer(int userId) {
		return repository.getApplicationsByEmployer(userId);
	}


	public List<Application> getApplicationsByJob(int jobId) {
		return repository.getApplicationsByJob(jobId);
	}


	public Application getApplication(int jobId, int userId) {
		return repository.getApplication(jobId, userId);
	}
	

}
