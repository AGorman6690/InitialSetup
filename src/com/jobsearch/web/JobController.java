package com.jobsearch.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.App;
import com.jobsearch.model.Item;
import com.jobsearch.user.service.JobSearchUser;

@Controller
@SessionAttributes({"user", "app"})
public class JobController {

	@Autowired
	JobServiceImpl jobService;
	
	@Autowired
	CategoryServiceImpl categoryService;

	@RequestMapping(value = "addJob", method = RequestMethod.GET)
	@ResponseBody
	public String addJob(ModelAndView model, @RequestParam String jobName, @RequestParam int userId) {

		// Add the job to the database
		jobService.addJob(jobName, userId);
		
		//Get the user's jobs
		Item item = new Item();
		item.setJobs(jobService.getJobs(userId));

		return JSON.stringify(item);

	}
	
	
	@RequestMapping(value = "/getJobs", method = RequestMethod.GET)
	@ResponseBody
	public String getJobs(ModelAndView model, @RequestParam int userId){

		Item item = new Item();		
		item.setJobs(jobService.getJobs(userId));
		return JSON.stringify(item);
		
	}

	// @RequestMapping(value = "/markJobComplete", method = RequestMethod.GET)
	// @ResponseBody
	// public ModelAndView markJobComplete(ModelAndView model, @RequestBody
	// final DataBaseItem item,
	// @ModelAttribute App app){
	@RequestMapping(value = "/markJobComplete", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public String markJobComplete(ModelAndView model, @RequestParam int jobId, @ModelAttribute App app,
			@ModelAttribute("user") JobSearchUser user) {
		// Update database
		jobService.updateJobComplete(jobId);

		// Update user's active jobs to reflect the changes
		user.setActiveJobs(jobService.getJobs(user.getUserId(), true));

		return JSON.stringify(user);

	}

	@RequestMapping(value = "findEmployees", method = RequestMethod.GET)
	public ModelAndView findEmployees(ModelAndView model, @ModelAttribute JobSearchUser user) {
		model.addObject("user", user);
		model.setViewName("FindEmployees");
		return model;

	}

	@RequestMapping(value = "findJobs", method = RequestMethod.GET)
	public ModelAndView findJobs(ModelAndView model, @ModelAttribute JobSearchUser user) {

		List<Category> categories = categoryService.getCategories();
		
		model.addObject("categories", categories);
		
		model.setViewName("FindJobs");
		return model;

	}
}
