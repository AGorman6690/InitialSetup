package com.jobsearch.application.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.model.application.ApplicationInvite;
import com.jobsearch.request.ApplyForJobRequest;
import com.jobsearch.request.MakeInitialOfferByEmployerRequest;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
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
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String applyForJob(@RequestBody ApplyForJobRequest request, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			applicationService.applyForJob(request, session);
			return "redirect:/user";
		} else {
			return "NotLoggedIn";
		}

	}
	
	
	@RequestMapping(value = "/application/{applicationId_withReferenceTo}/conflicting-applications",
		method = RequestMethod.POST)
	public String getConflictingApplications(@RequestBody List<String> dateStrings_toFindConflictsWith,
						@PathVariable( value = "applicationId_withReferenceTo") int applicationId_withReferenceTo,
						HttpSession session, Model model) {		
		
		applicationService.setConflictingApplicationsResponse(model, session, applicationId_withReferenceTo,
				dateStrings_toFindConflictsWith);		
		return "/wage_proposal/ConflictingApplications"; 
	}	
	

	

	
	@RequestMapping(value = "/job/{jobId}/make-an-offer/initialize", method = RequestMethod.GET)
	public String getHtml_employerMakeFirstOffer(Model model, HttpSession session,
												@PathVariable(value = "jobId") int jobId) {
		
		if(applicationService.setModel_employerMakeFirstOffer(model, session, jobId))
			return "/wage_proposal/AjaxResponse_Proposal_NEW";
		else return SessionContext.get404Page();
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
		
		userService.setModel_makeOffer(model, userId, jobId, session);
		
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
