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

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.user.service.JobSearchUser;

@Controller
@SessionAttributes({ "user" })
public class JobController {

	@Autowired
	JobServiceImpl jobService;
	
	@Autowired
	CategoryServiceImpl categoryService;
	
	@RequestMapping(value = "/addJob", method = RequestMethod.GET)
	@ResponseBody
	public String addJob(@RequestParam String jobName, @RequestParam int userId,
							@RequestParam int categoryId) {

		// Add the job to the job table
		jobService.addJob(jobName, userId);
		
		//Get the id of the newly created job
		Job newJob = new Job();
		newJob = jobService.getJobByJobNameAndUser(jobName, userId);
		
		//Add the category to the job
		categoryService.addCategoryToJob(newJob.getId(), categoryId);

		return JSON.stringify(jobService.getJobsByUser(userId));

	}
	
	@RequestMapping(value = "/applyForJob", method = RequestMethod.GET)
	@ResponseBody
	public String applyForJob(@RequestParam int jobId, @RequestParam int userId) {

		// Add application to database
		jobService.applyForJob(jobId, userId);
		
		return JSON.stringify(jobService.getApplicationsByUser(userId));

	}
	
	
	@RequestMapping(value = "/viewActiveJob_Employer", method = RequestMethod.GET)
	public ModelAndView viewActiveJob_Employer(ModelAndView model) {
		model.setViewName("ViewActiveJob_Employer");
		return model;
	}
	
	
	@RequestMapping(value = "/getJobsByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getJobsByUser(@RequestParam int userId){
		return JSON.stringify(jobService.getJobsByUser(userId));
		
	}
	
	@RequestMapping(value = "/getJobCountByCategory", method = RequestMethod.GET)
	@ResponseBody
	public String getJobCountByCategory(@RequestParam int categoryId){
		return JSON.stringify(jobService.getJobCountByCategory(categoryId));
		
	}
	

	@RequestMapping(value = "/getActiveJobsByUser_AppCat", method = RequestMethod.GET)
	@ResponseBody
	public String getActiveJobsByUser_AppCat(@RequestParam int userId){
		//For each active job, set its category and applications
		
		return JSON.stringify(jobService.getActiveJobsByUser_AppCat(userId));		
	}
	
	@RequestMapping(value = "/getCompletedJobsByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getCompletedJobsByUser(@RequestParam int userId){
		//For each active job, set its category and applications
		
		return JSON.stringify(jobService.getCompletedJobsByUser(userId));	
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
	
	@RequestMapping(value = "/getJobOffersByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getJobOffersByUser(@RequestParam int userId){
		return JSON.stringify(jobService.getJobOffersByUser(userId));	
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
		jobService.markJobComplete(jobId);
		
		return JSON.stringify(jobService.getJobsByUser(user.getUserId()));
	}
	

	
}