package com.jobsearch.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.repository.ApplicationRepository;
import com.jobsearch.user.service.UserServiceImpl;

@Service
public class ApplicationServiceImpl {
	
	@Autowired
	ApplicationRepository repository;
	
	
	@Autowired
	UserServiceImpl userService;



	public List<Application> getApplicationsByJob(int jobId) {
		
		List<Application> applications = repository.getApplicationsByJob(jobId);
		
		//For each application, set the applicant
		for(Application application : applications){
			application.setApplicant(userService.getUser(application.getUserId()));
			
		}
		
		return applications;
	}


	public Application getApplication(int jobId, int userId) {
		return repository.getApplication(jobId, userId);
	}


	public void updateStatus(int applicationId, int status) {
		repository.updateStatus(applicationId, status);
		
		//If hired
		if (status == 3){
			Application application = getApplication(applicationId);
			userService.hireApplicant(application.getUserId(), application.getJobId());
		}
		
	}


	public Application getApplication(int applicationId) {
		return repository.getApplication(applicationId);
	}
	

}
