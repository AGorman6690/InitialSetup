package com.jobsearch.utilities;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.WorkDay;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.repository.UserRepository;

// Not sure 
@Service
public class VerificationServiceImpl {
	
	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	ApplicationServiceImpl applicationService;	
	

	public boolean isPositiveNumber(Integer number){
		
		if(number != null && number > 0) return true;
		else return false;
	}
	
	public boolean isPositiveNumber(Double number){
		
		if(number != null && number > 0) return true;
		else return false;
	}

	public boolean didSessionUserPostJob(HttpSession session, int jobId) {

		Job job = jobService.getJob(jobId);
		
		if(job != null  && 
				job.getUserId() == SessionContext.getUser(session).getUserId()) return true;
		
		else return false;
	}

	public <T> boolean isListPopulated(List<T> list) {
		
		if(list != null && list.size() > 0) return true;
		else return false;
	}

	public boolean isValidLocation(Job job) {		
		
		if((job.getStreetAddress() == null || job.getStreetAddress().matches("")) && 
				(job.getCity() == null || job.getCity().matches("")) && 
				(job.getState() == null || job.getState().matches("")) && 
				(job.getZipCode() == null || job.getZipCode().matches(""))){
			return false;
		}
		else return true;
	
	}

	public boolean didUserApplyForJob(int jobId, int userId) {

		
		Application application = applicationService.getApplication(jobId, userId);
		
		if(application != null) return true;
		else return false;
	}
	
}
