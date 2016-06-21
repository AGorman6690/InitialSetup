package com.jobsearch.job.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.application.service.ApplicationRequestDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.CompletedJobResponseDTO;
import com.jobsearch.job.service.SubmitJobPostingRequestDTO;
import com.jobsearch.job.service.FilterJobRequestDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
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

	@ResponseBody
	@RequestMapping(value = "/jobs/post", method = RequestMethod.POST)
	public void addJob(@RequestBody SubmitJobPostingRequestDTO postingDto, ModelAndView model) {

		jobService.addPosting(postingDto);
	}

	@RequestMapping(value = "/jobs/filter", method = RequestMethod.GET)
	@ResponseBody
	public String getFilteredJobs(@RequestParam(required = true) int radius,
			@RequestParam(required = true) String fromAddress,
			@RequestParam(value = "categoryId", required = false) int[] categoryIds,
			@RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime,
			@RequestParam(required = false) boolean beforeStartTime,
			@RequestParam(required = false) boolean beforeEndTime,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) boolean beforeStartDate,
			@RequestParam(required = false) boolean beforeEndDate,
			@RequestParam(value = "day", required = false) List<String> workingDays,
			@RequestParam(required = false, defaultValue= "-1") Double duration,
			@RequestParam(required = false) boolean lessThanDuration,
			@RequestParam(required = false, defaultValue = "25") Integer returnJobCount) 
			{
//
		FilterJobRequestDTO filter = new FilterJobRequestDTO(radius, fromAddress, categoryIds, startTime, endTime, beforeStartTime,
				beforeEndTime, startDate, endDate, beforeStartDate, beforeEndDate, workingDays, duration,
				lessThanDuration, returnJobCount);


		filter.setJobs(jobService.getFilteredJobs(filter)); // , startDate,
															// endDate));

		// Set the filter criteria specified by user

		return JSON.stringify(filter);

	}

	@ResponseBody
	@RequestMapping(value = "/job/apply", method = RequestMethod.POST)
	public void applyForJob(@RequestBody ApplicationRequestDTO applicationDto, ModelAndView model) {

		applicationService.applyForJob(applicationDto);

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
	public ModelAndView getEmployeeWorkHistory(@RequestParam int userId, @RequestParam(required = false) Integer jobId,
			@RequestParam int viewContext, ModelAndView model) {

		// ***************
		// Context values:
		// 0 = "viewing employee as a prospective applicant for a particular
		// job"
		// 1 = "viewing employee from searching for employees

		JobSearchUser employee = userService.getUser(userId);
		employee.setEndorsements(userService.getUsersEndorsements(userId));
		if (jobId != null) {
			employee.setApplication(applicationService.getApplication(jobId, userId));
		}
		model.addObject("worker", employee);

		// Job consideredForJob = jobService.getJob(jobId);
		// model.addObject("consideredForJob", consideredForJob);

		List<CompletedJobResponseDTO> completedJobDtos = jobService.getCompletedJobsByEmployee(userId);
		model.addObject("completedJobDtos", completedJobDtos);

		String context = null;
		if (viewContext == 0) {
			context = "viewingApplication";
		} else if (viewContext == 1) {
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