package com.jobsearch.utilities;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.jobsearch.model.Application;
import com.jobsearch.model.Job;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.repository.UserRepository;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.ProposalServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.session.SessionContext;

// Not sure 
@Service
public class VerificationServiceImpl {
		
	@Autowired
	UserServiceImpl userService;
	@Autowired
	JobServiceImpl jobService;
	@Autowired
	ApplicationServiceImpl applicationService;	
	@Autowired
	ProposalServiceImpl proposalService;
	
	// Refactor these to the NumberUtility
	// *****************************************************************
	public boolean isPositiveNumber(Integer number){
		
		if(number != null && number > 0) return true;
		else return false;
	}
	
	public boolean isPositiveNumber(Double number){
		
		if(number != null && number > 0) return true;
		else return false;
	}
	
	
	public boolean isPositiveNumber(String number) {
		
		if(number != null && number != ""){
			Double number_fromString = Double.valueOf(number);
			if(number_fromString == null || number_fromString <= 0) return false;
			else return true;
		}else return false;
	}

	public boolean isPositiveNumberOrZero(Integer number){
		
		if(number != null && number >= 0) return true;
		else return false;
	}
	// *****************************************************************

	@Deprecated
	public boolean didSessionUserPostJob(HttpSession session, int jobId) {

		Job job = jobService.getJob(jobId);
		
		return this.didSessionUserPostJob(session, job);

	}

	
	public boolean didSessionUserPostJob(HttpSession session, Job job) {
		
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

	@Deprecated
	public boolean didUserApplyForJob(int jobId, int userId) {

		
		Application application = applicationService.getApplication(jobId, userId);
		
		if(application != null) return true;
		else return false;
	}

	public boolean isCurrentProposalProposedToSessionUser(int applicationId, HttpSession session) {
		
		Proposal currentProposal = proposalService.getCurrentProposal(applicationId);
		if(currentProposal.getProposedToUserId() == SessionContext.getUser(session).getUserId())
			return true;
		else return false;
	}


	public boolean isSessionUserAnEmployee(HttpSession session, Integer jobId) {
		JobSearchUser employee = userService.getEmployee(jobId, SessionContext.getUser(session).getUserId());
		if(employee != null) return true;
		else return false;
	}

}
