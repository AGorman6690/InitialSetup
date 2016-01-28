package com.jobsearch.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.user.service.JobSearchUser;

@Controller
@SessionAttributes({ "user" })
public class JobController {

	@Autowired
	JobServiceImpl jobService;
	
	@RequestMapping(value = "/addJob", method = RequestMethod.GET)
	@ResponseBody
	public String addJob(@RequestParam String jobName, @RequestParam int userId) {

		// Add the job to the database
		jobService.addJob(jobName, userId);

		return JSON.stringify(jobService.getJobsByUser(userId));

	}
	
	@RequestMapping(value = "/applyForJob", method = RequestMethod.GET)
	@ResponseBody
	public String applyForJob(@RequestParam int jobId, @RequestParam int userId) {

		// Add application to database
		jobService.applyForJob(jobId, userId);
		
		return JSON.stringify(jobService.getApplicationsByUser(userId));

	}
	
	
	@RequestMapping(value = "/getJobsByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getJobsByUser(ModelAndView model, @RequestParam int userId){
		return JSON.stringify(jobService.getJobsByUser(userId));
		
	}
	
	@RequestMapping(value = "/getJobsByCategory", method = RequestMethod.GET)
	@ResponseBody
	public String getJobsByCategory(@RequestParam int categoryId){
		return JSON.stringify(jobService.getJobsByCategory(categoryId));		
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

	@RequestMapping(value = "/markJobComplete", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public String markJobComplete(@RequestParam int jobId, @ModelAttribute("user") JobSearchUser user) {
		
		// Update database
		jobService.updateJobComplete(jobId);
		
		return JSON.stringify(jobService.getJobsByUser(user.getUserId()));

	}
}