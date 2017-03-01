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
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WageProposalDTO;
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


	@RequestMapping(value = "/application/status/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateStatus(@RequestBody ApplicationDTO applicationDto) {
		applicationService.updateApplicationStatus(applicationDto.getApplication().getApplicationId(),
								applicationDto.getNewStatus());
	}

	@RequestMapping(value = "/wage-proposal/counter", method = RequestMethod.POST)
	@ResponseBody
	public void counterOffer(@RequestBody WageProposalDTO wageProposalDto, HttpSession session) {

		applicationService.insertCounterOffer(wageProposalDto, session);
	}

	@RequestMapping(value = "/wage-proposal/accept/employer", method = RequestMethod.POST)
	@ResponseBody
	public String acceptOffer_Employer(@RequestParam(name = "wageProposalId", required = true) int wageProposalId,
										@RequestParam(name = "days", required = false) Integer days,
										@RequestParam(name = "hours", required = false) Integer hours,
										@RequestParam(name = "minutes", required = false) Integer minutes,
										HttpSession session) {

		applicationService.acceptWageProposal_Employer(wageProposalId, session, days, hours, minutes);

		WageProposal wageProposal = applicationService.getWageProposal(wageProposalId);
		return JSON.stringify(wageProposal);

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
	public void employerInitiateContact(@RequestBody ApplicationDTO applicationDto, HttpSession session) {

		applicationService.initiateContact_byEmployer(applicationDto, session);
	}

	@RequestMapping(value = "/wage-proposal/accept/applicant", method = RequestMethod.GET)
	public String acceptOffer(@RequestParam(name = "wageProposalId") int wageProposalId,
								HttpSession session) {

		applicationService.acceptWageProposal_Employee(wageProposalId, session);

		return "redirect:/user/profile";

	}
	
	@RequestMapping(value = "/wage-proposal/decline", method = RequestMethod.GET)
	public String declineOffer(@RequestParam(name = "wageProposalId") int wageProposalId,
								HttpSession session) {

		applicationService.declineWageProposalStatus(wageProposalId, session);

		return "redirect:/user/profile";
	}
}
