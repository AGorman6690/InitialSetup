package com.jobsearch.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.sql.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;


import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.CreateJobDTO;
import com.jobsearch.job.service.FilterDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.GoogleClient;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
@SessionAttributes({ "user", "job" })
public class JobController {

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	CategoryServiceImpl categoryService;

//<<<<<<< HEAD
//	@RequestMapping(value = "/job/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
////	public ModelAndView addJob(@RequestBody CreateJobDTO jobDto, ModelAndView model) {
//	public ModelAndView addJob(@ModelAttribute("job") CreateJobDTO jobDto, ModelAndView model) {
//		
//		//Add the job to the job table
//		@SuppressWarnings("unused")
//		List<Job> jobsCreatedByUser = jobService.addJob(jobDto);
//	 
//		model.setViewName("EmployerProfile");
//		//return JSON.stringify(jobsCreatedByUser);
//		return model;
//=======
	@Autowired
	ApplicationServiceImpl applicationService;

	@RequestMapping(value = "/jobs/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addJob(@RequestBody List<CreateJobDTO> jobDtos, ModelAndView model) {

		
		
		// Add the job to the job table
		jobService.addJob(jobDtos);

	}
	

//	public String getFilteredJobs(@RequestParam int radius, @RequestParam String fromAddress, 
//									@RequestParam(value="category") int[] categoryIds,
//									@RequestParam DateTime startDate,
//									@RequestParam DateTime endDate ) {
	
	@RequestMapping(value = "/jobs/filter", method = RequestMethod.GET)
	@ResponseBody
	public String getFilteredJobs(@RequestParam int radius, @RequestParam String fromAddress, 
			@RequestParam(value="categoryId") int[] categoryIds, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam boolean beforeStartTime, 
			@RequestParam boolean beforeEndTime, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam boolean beforeStartDate,
			@RequestParam boolean beforeEndDate) {


		
		FilterDTO filter = new FilterDTO(radius, fromAddress, categoryIds, startTime, endTime,
				beforeStartTime, beforeEndTime, startDate, endDate, beforeStartDate, beforeEndDate);

		filter.setJobs(jobService.getFilteredJobs(filter)); //, startDate, endDate));
		
		//Set the filter criteria specified by user

		

		return JSON.stringify(filter);

	}
	
	@RequestMapping(value = "/job/{jobId}/user/{userId}/apply", method = RequestMethod.GET)
	public ModelAndView applyForJob(@PathVariable int jobId, @PathVariable int userId, ModelAndView model) {

		// Add application to database
		jobService.applyForJob(jobId, userId);
		
//		ModelAndView model = new ModelAndView();
		model.setViewName("EmployeeProfile");
		
		//return JSON.stringify(jobService.getApplicationsByUser(userId));
		return model;

	}
	
	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.GET)
	public ModelAndView getJob(@PathVariable int jobId, ModelAndView model) {
		
		

		Job selectedJob = jobService.getJob(jobId);

		model.addObject("job", selectedJob);
		model.setViewName("Job");
		return model;
	}

	@RequestMapping(value = "/job/edit", method = RequestMethod.GET)
	public ModelAndView viewEditJob(ModelAndView model) {

		// Job activeJob = jobService.getJob(jobId);
		// model.addObject("job", JSON.stringify(activeJob));

		model.setViewName("EditJob");
		return model;
	}

	@RequestMapping(value = "/getJobsByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getJobsByUser(@RequestParam int userId) {
		return JSON.stringify(jobService.getJobsByUser(userId));

	}
	
	@RequestMapping(value = "/getJobCountByCategory", method = RequestMethod.GET)
	@ResponseBody
	public String getJobCountByCategory(@RequestParam int categoryId) {
		return JSON.stringify(jobService.getJobCountByCategory(categoryId));

	}

	@RequestMapping(value = "/jobs/active/user", method = RequestMethod.GET)
	@ResponseBody
	public String getActiveJobsByUser(@RequestParam int userId) {
		// For each active job, set its category and applications

		return JSON.stringify(jobService.getActiveJobsByUser(userId));
	}

	@RequestMapping(value = "/jobs/completed/user", method = RequestMethod.GET)
	@ResponseBody
	public String getCompletedJobsByUser(@RequestParam int userId) {
		// For each active job, set its category and applications

		return JSON.stringify(jobService.getCompletedJobsByUser(userId));
	}

	@RequestMapping(value = "/getJobsByCategory", method = RequestMethod.GET)
	@ResponseBody
	public String getJobsByCategory(@RequestParam int categoryId) {
		return JSON.stringify(jobService.getJobsByCategory(categoryId));
	}

	@RequestMapping(value = "/getJobOffersByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getJobOffersByUser(@RequestParam int userId) {
		return JSON.stringify(jobService.getJobOffersByUser(userId));
	}

	@RequestMapping(value = "/job/{jobId}/markComplete", method = RequestMethod.PUT)
	@ResponseBody
	public void markJobComplete(@PathVariable("jobId") int jobId) {
		// Update database
		jobService.markJobComplete(jobId);
	}

	@RequestMapping(value = "/job/{jobId}/rateEmployees", method = RequestMethod.GET)
	public ModelAndView viewRateEmployees(@PathVariable int jobId, ModelAndView model) {

		List<JobSearchUser> employees = userService.getEmployeesByJob(jobId);		
		model.addObject("employees", employees);
		
		Job job = jobService.getJob(jobId);
		model.addObject("job", job);

		model.setViewName("RateEmployees");
		return model;
	}

	@RequestMapping(value = "/job/{jobId}/hire/user/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public String hireApplicant(@PathVariable int jobId, @PathVariable int userId) {

		// Add employment to the database
		userService.hireApplicant(userId, jobId);

		Job activeJob = jobService.getJob(jobId);

		return JSON.stringify(activeJob);
	}
}