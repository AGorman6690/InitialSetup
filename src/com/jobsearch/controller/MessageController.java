package com.jobsearch.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jobsearch.model.Application;
import com.jobsearch.repository.ProposalRepository;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.MessageServiceImpl;
import com.jobsearch.service.RatingServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.service.WorkDayServiceImpl;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.VerificationServiceImpl;

@Controller
@RequestMapping( value="/message")
public class MessageController {
	
//	@Autowired
//	ProposalRepository repository;
	@Autowired
	JobServiceImpl jobService;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	MessageServiceImpl messageService;
//	@Autowired
//	VerificationServiceImpl verificationService;
//	@Autowired
//	GoogleClient googleClient;
//	@Autowired
//	RatingServiceImpl ratingService;
//	@Autowired
//	WorkDayServiceImpl workDayService;	
	
	@RequestMapping(value = "/employee/{userId}/left/job/{jobId}/acknowledge", method = RequestMethod.GET)
	public String acknowledgeEmployeeLeftJob(@PathVariable(value = "jobId") int jobId,
											@PathVariable(value = "userId") int userId,
											HttpSession session) {

		messageService.acknowledgeEmployeeLeft(jobId, userId, session);
		
		return "redirect:/job/" + jobId + "?c=waiting";
	}
	
	@RequestMapping(value = "/application/{applicationId}/all-positions-filled/acknowledge", method = RequestMethod.GET)
	public String acknowledgeAllPositionsAreFilled(@PathVariable(value = "applicationId") int applicationId,
								HttpSession session) {
		
		applicationService.updateFlag_applicantAcknowledgesAllPositionsAreFilled(session, applicationId);
		
		return "redirect:/user/";
		
	}
	
	@RequestMapping(value = "application-closed-due-to-all-positions-filleed/{applicationId}/acknowledge"
			, method = RequestMethod.GET)
	public String acknowledge_applicationClosed_employerFilledAllPositions(
			@PathVariable(value = "applicationId") int applicationId, HttpSession session) {

		applicationService.updateApplicationFlag(applicationId,
				Application.FLAG_APPLICANT_ACKNOWLEDGED_ALL_POSITIONS_ARE_FILLED, 1);
		
		return "redirect:/user";
	}
	
	@RequestMapping(value = "/employer-removed-you-from-job/{jobId}/acknowledge", method = RequestMethod.GET)
	public String acknowledgeEmployerRemovedYouFromJob(@PathVariable(value = "jobId") int jobId,
											HttpSession session) {

		jobService.updateEmploymentFlag(jobId, SessionContext.getUser(session).getUserId(),
				"Flag_EmployeeAcknowledgedEmployerRemoval", 1);
		
		return "redirect:/user";
	}

}
