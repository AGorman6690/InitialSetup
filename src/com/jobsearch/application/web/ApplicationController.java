package com.jobsearch.application.web;

import java.util.ArrayList;
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

import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.job.service.JobDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WageProposalDTO;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.model.application.ApplicationInvite;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
public class ApplicationController {

	@Autowired
	ApplicationServiceImpl applicationService;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;

	

	@ResponseBody
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String applyForJob(@RequestBody ApplicationDTO applicationDto, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			applicationService.applyForJob(applicationDto, session);
			return "redirect:/user/profile";
		} else {
			return "NotLoggedIn";
		}

	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/employment-proposal/respond", method = RequestMethod.POST)
	public String respondToEmploymentProposal(@RequestBody EmploymentProposalDTO employmentProposalDto,
												@RequestParam(name = "c", required = true) String c, 
												HttpSession session) {

		applicationService.respondToEmploymentProposal(employmentProposalDto, session, c);
	
		return ""; 

	}
	
	@RequestMapping(value = "/application/{applicationId}/current-proposal", method = RequestMethod.GET)
	public String getProposal(@PathVariable(value = "applicationId") int applicationId,
								Model model, HttpSession session) {
		applicationService.setModel_ViewCurrentProposal(model, session, applicationId);
		return "/wage_proposal/AjaxResponse_Proposal";
	}
	
	@RequestMapping(value = "/job/{jobId}/make-an-offer/initialize", method = RequestMethod.GET)
	public String getHtml_employerMakeFirstOffer(Model model, HttpSession session,
												@PathVariable(value = "jobId") int jobId) {
		
		if(applicationService.setModel_employerMakeFirstOffer(model, session, jobId))
			return "/wage_proposal/AjaxResponse_Proposal";
		else return SessionContext.get404Page();
	}
	
	@ResponseBody
	@RequestMapping(value = "/application/{applicationId}/proposed-work-days", method = RequestMethod.GET)
	public String getWorkDayDtos_proposedWorkDays(@PathVariable(value = "applicationId") int applicationId,
													HttpSession session) {
		
		List<WorkDayDto> workDayDtos = applicationService.getWorkDayDtos_proposedWorkDays(applicationId, session);
		
		return JSON.stringify(workDayDtos);
	}
	
	
	@RequestMapping(value = "/application/{jobId}/user/{userId}/status", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicationStatus_ByUserAndJob(@PathVariable(value = "jobId") int jobId,
								@PathVariable(value = "userId") int userId,
								HttpSession session) {
		
		// ***************************************
		// ***************************************
		// Pretty this up
		// ***************************************
		// ***************************************
		
		
		JobDTO jobDto = jobService.getJobDTO_DisplayJobInfo(jobId);
		jobDto.setApplicationStatus(applicationService.getApplicationStatus(jobId, userId, session));
//		jobDto.setJob(jobService.getJob(jobId));
		jobDto.setWorkDays(jobService.getWorkDays(jobId));
		
		return JSON.stringify(jobDto);
//		if(jobDto.getApplicationStatus() != null) return JSON.stringify(jobDto);
//		else return SessionContext.get404Page();

	}
	
	@RequestMapping(value = "/employer/initiate-contact", method = RequestMethod.POST)
	@ResponseBody
	public String initiateContact_byEmployer(@RequestBody ApplicationDTO applicationDto, HttpSession session) {

		applicationService.initiateContact_byEmployer(applicationDto, session);
		
		return JSON.stringify("");
	}

	
	@RequestMapping(value = "/employer/initiate-contact/application-invite", method = RequestMethod.POST)
	@ResponseBody
	public void inviteToApply(@RequestBody ApplicationInvite applicationInvite, HttpSession session) {

		applicationService.insertApplicationInvite(applicationInvite, session);
	}
	
	
	@RequestMapping(value = "/application/status/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateStatus(@RequestBody ApplicationDTO applicationDto) {
		applicationService.updateApplicationStatus(applicationDto.getApplication().getApplicationId(),
								applicationDto.getNewStatus());
	}
}
