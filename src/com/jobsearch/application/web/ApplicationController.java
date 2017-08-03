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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.model.application.ApplicationInvite;
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
	public String applyForJob(@RequestBody ApplicationDTO applicationDto, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			applicationService.applyForJob(applicationDto, session);
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
	
	@RequestMapping(value = "/application/{applicationId}/all-positions-filled/acknowledge", method = RequestMethod.GET)
	public String acknowledgeAllPositionsAreFilled(@PathVariable(value = "applicationId") int applicationId,
								HttpSession session) {
		
		applicationService.updateFlag_applicantAcknowledgesAllPositionsAreFilled(session, applicationId);
		
		return "redirect:/user/";
		
	}
	

	
	@RequestMapping(value = "/job/{jobId}/make-an-offer/initialize", method = RequestMethod.GET)
	public String getHtml_employerMakeFirstOffer(Model model, HttpSession session,
												@PathVariable(value = "jobId") int jobId) {
		
		if(applicationService.setModel_employerMakeFirstOffer(model, session, jobId))
			return "/wage_proposal/AjaxResponse_Proposal_NEW";
		else return SessionContext.get404Page();
	}
	
	@ResponseBody
	@RequestMapping(value = "/application/{applicationId}/proposed-work-days", method = RequestMethod.GET)
	public String getWorkDayDtos_proposedWorkDays(@PathVariable(value = "applicationId") int applicationId,
													HttpSession session) {
		
		List<WorkDayDto> workDayDtos = applicationService.getWorkDayDtos_proposedWorkDays(applicationId, session);
		
		return JSON.stringify(workDayDtos);
	}
	
	
	@RequestMapping(value = "application-closed-due-to-all-positions-filleed/{applicationId}/acknowledge"
			, method = RequestMethod.GET)
	public String acknowledge_applicationClosed_employerFilledAllPositions(
			@PathVariable(value = "applicationId") int applicationId, HttpSession session) {

		applicationService.updateApplicationFlag(applicationId,
				Application.FLAG_APPLICANT_ACKNOWLEDGED_ALL_POSITIONS_ARE_FILLED, 1);
		
		return "redirect:/user";
	}
	@RequestMapping(value = "/employer/make-initial-offer", method = RequestMethod.POST)
	@ResponseBody
	public String initiateContact_byEmployer(@RequestBody ApplicationDTO applicationDto,
			HttpSession session) {

		applicationService.initiateContact_byEmployer(applicationDto, session);
		
		return "";
	}

	
	@RequestMapping(value = "/employer/initiate-contact/application-invite", method = RequestMethod.POST)
	@ResponseBody
	public void inviteToApply(@RequestBody ApplicationInvite applicationInvite, HttpSession session) {

		applicationService.insertApplicationInvite(applicationInvite, session);
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
