package com.jobsearch.application.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.WageProposalCounterDTO;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
//@SessionAttributes({ "user" })
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
	public void updateStatus(@RequestParam(name = "applicationId") int applicationId,
							@RequestParam(name = "status") int status) {
		
		applicationService.updateApplicationStatus(applicationId, status);
	}
	
	@RequestMapping(value = "/desired-pay/counter", method = RequestMethod.POST)
	@ResponseBody
	public void counterOffer(@RequestBody WageProposalCounterDTO dto) {
		
		applicationService.insertCounterOffer(dto);
	}
	
	@RequestMapping(value = "/desired-pay/accept", method = RequestMethod.POST)
	@ResponseBody
	public void acceptOffer(@RequestParam(name = "wageProposalId") int wageProposalId) {
		
		applicationService.acceptWageProposal(wageProposalId);
	}	
	
	@RequestMapping(value = "/desired-pay/decline", method = RequestMethod.POST)
	@ResponseBody
	public void declineOffer(@RequestParam(name = "wageProposalId") int wageProposalId) {
		
		
		applicationService.declineWageProposalStatus(wageProposalId);
		
		
	}		

}
