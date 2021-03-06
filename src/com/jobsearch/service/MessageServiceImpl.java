package com.jobsearch.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.google.GoogleClient;
import com.jobsearch.repository.JobRepository;
import com.jobsearch.utilities.VerificationServiceImpl;

@Service
public class MessageServiceImpl {
	@Autowired
	JobRepository repository;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	VerificationServiceImpl verificationService;
	@Autowired
	GoogleClient googleClient;
	@Autowired
	ProposalServiceImpl proposalService;
	@Autowired
	RatingServiceImpl ratingService;
	@Autowired
	WorkDayServiceImpl workDayService;
	@Autowired
	JobServiceImpl jobService;
	
	public void acknowledgeEmployeeLeft(int jobId, int userId, HttpSession session) {
		if(verificationService.didSessionUserPostJob(session, jobId)){
			jobService.updateEmploymentFlag(jobId, userId, "Flag_EmployerAcknowledgedEmployeeLeftJob", 1);
		}
		
	}
}
