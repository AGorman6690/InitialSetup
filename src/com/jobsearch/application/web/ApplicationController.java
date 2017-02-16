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
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WageProposalCounterDTO;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
public class ApplicationController {

	@Autowired
	ApplicationServiceImpl applicationService;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;


	@RequestMapping(value = "/applications/job/{jobId}", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicationsByJob(@PathVariable(value = "jobId") int jobId){
		return JSON.stringify(applicationService.getApplicationsByJob(jobId));
	}


	@RequestMapping(value = "/application/status/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateStatus(@RequestBody ApplicationDTO applicationDto) {
		applicationService.updateApplicationStatus(applicationDto.getApplication().getApplicationId(),
								applicationDto.getNewStatus());
	}

	@RequestMapping(value = "/desired-pay/counter", method = RequestMethod.POST)
	@ResponseBody
	public void counterOffer(@RequestBody WageProposalCounterDTO dto, HttpSession session) {

		applicationService.insertCounterOffer(dto, session);
	}

	@RequestMapping(value = "/employer/accept", method = RequestMethod.POST)
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

	@RequestMapping(value = "/desired-pay/accept", method = RequestMethod.POST)
	@ResponseBody
	public String acceptOffer(@RequestParam(name = "wageProposalId") int wageProposalId,
								HttpSession session) {

		applicationService.acceptWageProposal_Employee(wageProposalId, session);

		WageProposal wageProposal = applicationService.getWageProposal(wageProposalId);
		return JSON.stringify(wageProposal);

	}
	
	@RequestMapping(value = "/desired-pay/decline", method = RequestMethod.POST)
	@ResponseBody
	public String declineOffer(@RequestParam(name = "wageProposalId") int wageProposalId,
								HttpSession session) {

		applicationService.declineWageProposalStatus(wageProposalId, session);

		WageProposal wageProposal = applicationService.getWageProposal(wageProposalId);
		return JSON.stringify(wageProposal);
	}
}
