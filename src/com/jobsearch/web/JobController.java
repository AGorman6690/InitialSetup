package com.jobsearch.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.App;
import com.jobsearch.model.Item;
import com.jobsearch.user.service.JobSearchUser;

@Controller
public class JobController {

	@Autowired
	JobServiceImpl jobService;
	
	@RequestMapping(value = "addJob", method = RequestMethod.GET)
	@ResponseBody
	public String addJob(ModelAndView model, @RequestParam String jobName, @RequestParam int userId) {

		// Add the job to the database
		jobService.addJob(jobName, userId);

		return JSON.stringify(jobService.getJobs(userId));

	}
	
	
	@RequestMapping(value = "/getJobsByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getJobsByUser(ModelAndView model, @RequestParam int userId){
		return JSON.stringify(jobService.getJobs(userId));
		
	}
	
	@RequestMapping(value = "/getJobsByCategory", method = RequestMethod.GET)
	@ResponseBody
	public String getJobsByCategory(@RequestParam int categoryId){

		Item item = new Item();		
		item.setJobs(jobService.getJobsByCategory(categoryId));
		return JSON.stringify(item);
		
	}
	
	@RequestMapping(value = "/getApplicationsByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicationsByUser(@RequestParam int userId){
		return JSON.stringify(jobService.getApplicationsByUser(userId));	
	}
	
	@RequestMapping(value = "/getEmploymentByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getEmploymentByUser(@RequestParam int userId){
		return JSON.stringify(jobService.getEmploymentByUser(userId));		
	}

	// @RequestMapping(value = "/markJobComplete", method = RequestMethod.GET)
	// @ResponseBody
	// public ModelAndView markJobComplete(ModelAndView model, @RequestBody
	// final DataBaseItem item,
	// @ModelAttribute App app){
	@RequestMapping(value = "/markJobComplete", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public String markJobComplete(@RequestParam int jobId, @ModelAttribute("user") JobSearchUser user) {
		
		// Update database
		jobService.updateJobComplete(jobId);
		
		return JSON.stringify(jobService.getJobsByUser(user.getUserId()));

	}

	@RequestMapping(value = "findEmployees", method = RequestMethod.GET)
	public ModelAndView findEmployees(ModelAndView model, @ModelAttribute JobSearchUser user) {

		model.setViewName("FindEmployees");
		return model;

	}

	@RequestMapping(value = "findJobs", method = RequestMethod.GET)
	public ModelAndView findJobs(ModelAndView model, @ModelAttribute JobSearchUser user) {

		model.setViewName("FindJobs");
		return model;

	}
}