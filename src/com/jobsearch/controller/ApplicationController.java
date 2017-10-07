package com.jobsearch.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.dtos.ApplicationDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.request.ApplyForJobRequest;
import com.jobsearch.request.ConflictingApplicationsRequest;
import com.jobsearch.request.MakeInitialOfferByEmployerRequest;
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
	
	@RequestMapping(value = "/application/employer-make-initial-offer", method = RequestMethod.POST)
	@ResponseBody
	public String initiateContact_byEmployer(@RequestBody MakeInitialOfferByEmployerRequest request,
			HttpSession session) {

		applicationService.makeInitialOfferByEmployer(request, session);		
		return "";
	}
	
	@RequestMapping(value = "/user/{userId}/make-offer/initialize", method = RequestMethod.GET)
	public String makeOffer_initialize(Model model, HttpSession session, 
			@PathVariable(value = "userId") int userId) {
		
		userService.setModel_makeOffer_initialize(model, userId, session);		
		return "/find_employees/MakeOffer_SelectJob";
	}
	
	@RequestMapping(value = "/user/{userId}/make-offer/job/{jobId}", method = RequestMethod.GET)
	public String makeOffer(Model model, HttpSession session, 
			@PathVariable(value = "userId") int userId,
			@PathVariable(value = "jobId") int jobId) {
		
		userService.setInitMakeOfferResponse(model, userId, jobId, session);
		
		return "/wage_proposal/AjaxResponse_Proposal_NEW";
	}
		
	@RequestMapping(value = "/application/{applicationId}/close", method = RequestMethod.GET)
	public String closeApplication(@PathVariable(value = "applicationId") int applicationId,
									HttpSession session) {
		
		JobSearchUser sessionUser = SessionContext.getUser(session);
		Job job = jobService.getJob_ByApplicationId(applicationId);
		if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE){
			if(verificationService.didUserApplyForJob(job.getId(), sessionUser.getUserId())){
				applicationService.closeApplication(applicationId);
			}
		}else{
			if(verificationService.didSessionUserPostJob(session, job.getId())){
				applicationService.closeApplication(applicationId);
			}
		}	
		
		return "redirect:/user";
	}
	
	@RequestMapping(value = "/application/status/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateStatus(@RequestBody ApplicationDTO applicationDto) {
		applicationService.updateApplicationStatus(applicationDto.getApplication().getApplicationId(),
								applicationDto.getNewStatus());
	}
}
