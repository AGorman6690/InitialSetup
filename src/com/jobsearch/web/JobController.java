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
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.CompletedJobDTO;
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

	@Autowired
	ApplicationServiceImpl applicationService;

	@RequestMapping(value = "/jobs/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addJob(@RequestBody List<CreateJobDTO> jobDtos, ModelAndView model) {

		jobService.addJob(jobDtos);

	}
	
	
	@RequestMapping(value = "/jobs/filter", method = RequestMethod.GET)
	@ResponseBody
	public String getFilteredJobs(@RequestParam int radius, @RequestParam String fromAddress, 
			@RequestParam(value="categoryId") int[] categoryIds, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam boolean beforeStartTime, 
			@RequestParam boolean beforeEndTime, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam boolean beforeStartDate,
			@RequestParam boolean beforeEndDate, @RequestParam(value="day") List<String> workingDays,
			@RequestParam double duration, @RequestParam boolean lessThanDuration,
			@RequestParam int returnJobCount) {


		
		FilterDTO filter = new FilterDTO(radius, fromAddress, categoryIds, startTime, endTime,
				beforeStartTime, beforeEndTime, startDate, endDate, beforeStartDate,
				beforeEndDate, workingDays, duration, lessThanDuration, returnJobCount);

		filter.setJobs(jobService.getFilteredJobs(filter)); //, startDate, endDate));
		
		//Set the filter criteria specified by user

		

		return JSON.stringify(filter);

	}
	
	@RequestMapping(value = "/job/apply",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView applyForJob(@RequestBody ApplicationDTO applicationDto, ModelAndView model) {

		applicationService.applyForJob(applicationDto);
		
//		ModelAndView model = new ModelAndView();
		model.setViewName("EmployeeProfile");
		
		//return JSON.stringify(jobService.getApplicationsByUser(userId));
		return model;

	}
	
	@RequestMapping(value = "/jobs/find", method = RequestMethod.GET)
	public ModelAndView viewFindJobs(ModelAndView model) {
		model.setViewName("FindJobs");
		return model;
	}

	
	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.GET)
	public ModelAndView getJob(@PathVariable int jobId, ModelAndView model) {

		Job selectedJob = jobService.getJob(jobId);
		
		//

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

	@RequestMapping(value = "/jobs/completed/employee", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getEmployeeWorkHistory(@RequestParam int userId, @RequestParam(required=false) Integer jobId,
												@RequestParam int c, ModelAndView model) {
		
		//***************
		//Context values:
		//0 = "viewing employee as a prospective applicant for a particular job"
		//1 = "viewing employee from searching for employees
		
		
		JobSearchUser employee = userService.getUser(userId);
		employee.setEndorsements(userService.getUsersEndorsements(userId));
		if(jobId != null){
			employee.setApplication(applicationService.getApplication(jobId, userId));
		}
		model.addObject("worker", employee);
		
//		Job consideredForJob = jobService.getJob(jobId);
//		model.addObject("consideredForJob", consideredForJob);
				
		List<CompletedJobDTO> completedJobDtos = jobService.getCompletedJobsByEmployee(userId);			
		model.addObject("completedJobDtos", completedJobDtos);
		
		String context = null; 
		if(c == 0){
			context = "viewingApplication";
		}else if (c == 1){
			context = "findEmployeeSearch";
		}		
		model.addObject("context", context);
		
		model.setViewName("EmployeeWorkHistory");
		return model;
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


}