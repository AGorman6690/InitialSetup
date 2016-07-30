package com.jobsearch.job.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.jobsearch.job.service.FilterJobResponseDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
//@SessionAttributes({ "user", "job" })
//@SessionAttributes( "loadedFilteredJobIds" )
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
	
	@SuppressWarnings({ "unchecked", "null" })
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
			@RequestParam(required = false, defaultValue = "25") Integer returnJobCount, 
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) boolean isAscending,
			@RequestParam(required = true) boolean isAppendingJobs,
//			@RequestParam(value = "id", required = false) int[] loadedJobIds ,
			HttpSession session, Model model
			){

		FilterJobRequestDTO request = new FilterJobRequestDTO(radius, fromAddress, categoryIds, startTime, endTime, beforeStartTime,
				beforeEndTime, startDate, endDate, beforeStartDate, beforeEndDate, workingDays, duration,
				lessThanDuration, returnJobCount, sortBy, isAscending);

		
		
		//*******************************************************************
		//For each job, imbed the lat/lnt its div.
		//Then set the map marders from these values
		//Remove the FilterJobResponseDTO if this works
		//*******************************************************************
		
		FilterJobResponseDTO response = new FilterJobResponseDTO();
		
		//If appending jobs, get the job ids that have already been rendered to the user
		List<Integer> alreadyLoadedFilteredJobIds = new ArrayList<Integer>();
		if(isAppendingJobs){				
			alreadyLoadedFilteredJobIds = (List<Integer>) session.getAttribute("loadedFilteredJobIds");
		}else{
			alreadyLoadedFilteredJobIds = null;
		}
		
		//From the request, set the jobs	
		List<Job> filteredJobs = new ArrayList<Job>();
		filteredJobs = jobService.getFilteredJobs(request, alreadyLoadedFilteredJobIds);
		
		
		//Get the job ids that were just queried
		List<Integer> loadedFilteredJobIds = filteredJobs.stream()
												.map(j -> j.getId()).collect(Collectors.toList());
		
		
		if(isAppendingJobs){			
			if(alreadyLoadedFilteredJobIds != null){
				loadedFilteredJobIds.addAll(alreadyLoadedFilteredJobIds);	
			}			
		}
		

		//Update the session variable
		session.setAttribute("loadedFilteredJobIds", loadedFilteredJobIds);
		
		model.addAttribute("loadedFilteredJobIds", loadedFilteredJobIds);
		
		
		//Set the html to render
		response.setHtml(jobService.getFilterdJobsResponseHtml(filteredJobs, request));

	
		return response.getHtml();

	}	

	@ResponseBody
	@RequestMapping(value = "/job/apply", method = RequestMethod.POST)
	public void applyForJob(@RequestBody ApplicationRequestDTO applicationDto,
								ModelAndView model, HttpSession session) {

		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		applicationDto.setUserId(user.getUserId());
		applicationService.applyForJob(applicationDto);

	}
	

	@RequestMapping(value = "/jobs/find", method = RequestMethod.GET)
	public String viewFindJobs(Model model, HttpSession session) {
		
		
		model.addAttribute("user", session.getAttribute("user"));
//		model.setViewName("FindJobs");
		return "FindJobs";
	}
	
	@RequestMapping(value = "/jobs/find/job/{jobId}", method = RequestMethod.GET)
	public String employeeViewJob(Model model, HttpSession session, @PathVariable int jobId) {
		
		Job job = jobService.getJobPostingInfo(jobId);
		model.addAttribute("job", job);
		model.addAttribute("user", session.getAttribute("user"));
//		model.setViewName("FindJobs");
		return "EmployeeViewJob";
	}

	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.GET)
	public String getJob(@PathVariable int jobId, Model model) {

		Job selectedJob = jobService.getEmployersJobProfile(jobId);


		
		

		//

		model.addAttribute("job", selectedJob);
//		model.setViewName("Job");
		return "EmployerViewJob";
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

		Job job = jobService.getEmployersJobProfile(jobId);
		model.addObject("job", job);

		model.setViewName("RateEmployees");
		return model;
	}

}