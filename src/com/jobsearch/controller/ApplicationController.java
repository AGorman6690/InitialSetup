package com.jobsearch.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.dtos.ApplicationDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.request.ApplyForJobRequest;
import com.jobsearch.request.ConflictingApplicationsRequest;
import com.jobsearch.responses.ApplyForJobResponse;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.VerificationServiceImpl;

@Controller
public class ApplicationController {

	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	JobServiceImpl jobService;
	@Autowired
	VerificationServiceImpl verificationService;	

	@ResponseBody
	@RequestMapping(value = "/application", method = RequestMethod.POST)
	public String applyForJob(@RequestBody ApplyForJobRequest request, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			ApplyForJobResponse response = applicationService.applyForJob(request, session);
			return JSON.stringify(response);
		} else {
			return "NotLoggedIn";
		}

	}	
	
	@RequestMapping(value = "/application/conflicting-applications",
		method = RequestMethod.POST)
	public String getConflictingApplications(@RequestBody ConflictingApplicationsRequest request,
						HttpSession session, Model model) {		
		
		applicationService.setConflictingApplicationsResponse(model, session, request);		
		return "/wage_proposal/ConflictingApplications"; 
	}	
	
	@RequestMapping(value = "/application/status/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateStatus(@RequestBody ApplicationDTO applicationDto) {
		applicationService.updateApplicationStatus(applicationDto.getApplication().getApplicationId(),
								applicationDto.getNewStatus());
	}
}
