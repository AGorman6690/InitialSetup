package com.jobsearch.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
@SessionAttributes({ "user" })
public class ApplicationController {


	@Autowired
	ApplicationServiceImpl applicationService;
	
	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	JobServiceImpl jobService;
	
	@RequestMapping(value = "/getApplicationsByEmployer", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicationsByEmployer(@RequestParam int userId){
		return JSON.stringify(applicationService.getApplicationsByEmployer(userId));	
	}
	
	
	@RequestMapping(value = "/getApplicationsByJob", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicationsByJob(@RequestParam int jobId){
		return JSON.stringify(applicationService.getApplicationsByJob(jobId));	
	}
	
	@RequestMapping(value = "/markApplicationViewed", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public void markApplicationViewed(@RequestParam int jobId, @RequestParam int userId) {
		
		// Update database
		applicationService.markApplicationViewed(jobId, userId);
		
	}
	
	@RequestMapping(value = "/markApplicationAccepted", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public String markApplicationAccepted(@RequestParam int jobId, @RequestParam int userId) {
		
		//Update database
		applicationService.markApplicationAccepted(jobId, userId);		
		userService.hireApplicant(userId, jobId);
		
		return JSON.stringify(jobService.getEmploymentByUser(userId));
		
	}

}
