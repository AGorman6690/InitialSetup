package com.jobsearch.application.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	public void initiateContact_byEmployer(@RequestBody ApplicationDTO applicationDto, HttpSession session) {

		applicationService.initiateContact_byEmployer(applicationDto, session);
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
