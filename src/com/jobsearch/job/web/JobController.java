package com.jobsearch.job.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.application.service.ApplicationRequestDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.FilterJobRequestDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.job.service.SubmitJobPostingDTO;
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
	public void postJobs(@RequestBody SubmitJobPostingDTO postingDto,
						HttpSession session, ModelAndView model) {
		
		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		jobService.addPosting(postingDto, user);
	}
	
	

	

	
	@RequestMapping(value ="/jobs/sort", method = RequestMethod.GET)
	@ResponseBody
	public String getSortedJobs(@RequestParam(name = "sortBy") String sortBy,
						@RequestParam(name = "isAscending") boolean isAscending,
						@RequestParam(name = "lat") float lat,
						@RequestParam(name = "lng") float lng,
						HttpSession session){
		
		//Set the request object
		FilterJobRequestDTO request = new FilterJobRequestDTO();
		request.setIsSortingJobs(true);		
		request.setSortBy(sortBy);
		request.setIsAscending(isAscending);
		request.setLat(lat);
		request.setLng(lng);
		
		//Get the html to display the already-rendered jobs in the requested order
		return jobService.getSortedJobsHTML(request, session);
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	@RequestMapping(value = "/jobs/filter", method = RequestMethod.GET)
	@ResponseBody
	public String getFilteredJobs(@RequestParam(name = "radius", required = true) int radius,
			@RequestParam(name = "fromAddress", required = true) String fromAddress,
			@RequestParam(name = "categoryId", value = "categoryId", required = false) int[] categoryIds,
			@RequestParam(name = "startTime", required = false) String startTime,
			@RequestParam(name = "endTime", required = false) String endTime,
			@RequestParam(name = "beforeStartTime", required = false) boolean beforeStartTime,
			@RequestParam(name = "beforeEndTime", required = false) boolean beforeEndTime,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate,
			@RequestParam(name = "beforeStartDate", required = false) boolean beforeStartDate,
			@RequestParam(name = "beforeEndDate", required = false) boolean beforeEndDate,
			@RequestParam(name = "d", value = "d", required = false) List<String> workingDays,
			@RequestParam(name = "duration", required = false, defaultValue= "-1") Double duration,
			@RequestParam(name = "lessThanDuration", required = false) boolean lessThanDuration,
			@RequestParam(name = "returnJobCount", required = false, defaultValue = "25") Integer returnJobCount, 
			@RequestParam(name = "sortBy", required = false) String sortBy,
			@RequestParam(name = "isAscending", required = false) boolean isAscending,
			@RequestParam(name = "isAppendingJobs", required = true) boolean isAppendingJobs,
//			@RequestParam(value = "id", required = false) int[] loadedJobIds ,
			HttpSession session
			){

		FilterJobRequestDTO request = new FilterJobRequestDTO(radius, fromAddress, categoryIds, startTime, endTime, beforeStartTime,
				beforeEndTime, startDate, endDate, beforeStartDate, beforeEndDate, workingDays, duration,
				lessThanDuration, returnJobCount, sortBy, isAscending, isAppendingJobs);
	
		
		return jobService.getVelocityTemplate_FilterJobs(request, session);
		//return jobService.getFilterdJobsResponseHtml(request, session, model);

	}	

	@ResponseBody
	@RequestMapping(value = "/job/apply", method = RequestMethod.POST)
	public String applyForJob(@RequestBody ApplicationRequestDTO applicationDto,
								HttpSession session) {

		if(userService.isLoggedIn(session)){
			applicationService.applyForJob(applicationDto, session);
			return "redirect:/user/profile";
		}
		else{
			return "NotLoggedIn";
		}
		
		

	}
	

	@RequestMapping(value = "/jobs/find", method = RequestMethod.GET)
	public String viewFindJobs(Model model, HttpSession session) {
		
		
		model.addAttribute("user", session.getAttribute("user"));
//		model.setViewName("FindJobs");
		return "FindJobs";
	}
	
	
	@RequestMapping(value = "/jobs/find-old", method = RequestMethod.GET)
	public String viewFindJobs_OLD(Model model, HttpSession session) {
		
		
		model.addAttribute("user", session.getAttribute("user"));
//		model.setViewName("FindJobs");
		return "FindJobs_NEW";
	}
	
	@RequestMapping(value = "/jobs/find/job/{jobId}", method = RequestMethod.GET)
	public String employeeViewJob(Model model, HttpSession session, @PathVariable(value = "jobId") int jobId) {
		
		jobService.setModel_ApplyForJob(model, jobId, session);

		return "EmployeeViewJobWhenFinding";
	}

	@RequestMapping(value = "/job/{jobId}/user/{employeeId}", method = RequestMethod.GET)
	public String getJobInfoAndAnswers_ByEmployee(@PathVariable(value = "jobId") int jobId,
													@PathVariable(value = "employeeId") int employeeId,
													Model model, HttpSession session) {
		
		
		
		jobService.setModel_EmployerViewJob_WhenViewingFromEmployeeWorkHistory(model, session, jobId, employeeId);

		
		return "EmployerViewJob_WhenViewingFromEmployeeWorkHistory";
		
	}

	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.GET)
	public String getJob(@PathVariable(value = "jobId") int jobId, Model model, HttpSession session) {
		
		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		
		//If employee
		if(user.getProfileId() == 1){
			
			jobService.setModelForEmployeeViewJobFromProfileJsp(model, jobId, user.getUserId());
			return "EmployeeViewJobFromProfile";
		//Else if employer
		}else if(user.getProfileId() == 2){
			jobService.setModel_EmployerViewJob(model, jobId, session);
			return "EmployerViewJob";	
		}
		
		return null;
		
	}
	
	@RequestMapping(value = "/completed/job/{jobId}", method = RequestMethod.GET)
	public String getCompletedJob(@PathVariable(value = "jobId") int jobId, Model model, HttpSession session) {
		
		JobSearchUser user = (JobSearchUser) session.getAttribute("user");

		jobService.setModel_EmployerViewCompletedJob(model, jobId, session);
		return "EmployerViewCompletedJob";	
		
		
	}
	
	
	@RequestMapping(value = "/job/{jobId}/update/status/{status}", method = RequestMethod.GET)
	public String updateJobStatus(@PathVariable(value = "status") int status,
								@PathVariable(value = "jobId") int jobId) {
		
		jobService.UpdateJobStatus(status, jobId);
		
		
		return "redirect:/user/profile";
		
	}
	

	@RequestMapping(value = "/job/edit", method = RequestMethod.GET)
	public ModelAndView viewEditJob(ModelAndView model) {

		// Job activeJob = jobService.getJob(jobId);
		// model.addObject("job", JSON.stringify(activeJob));

		model.setViewName("EditJob");
		return model;
	}



//	@RequestMapping(value = "/job/{jobId}/mark-complete", method = RequestMethod.GET)
//	public String markJobComplete(@PathVariable("jobId") int jobId, Model model) {
//		
//		return "RateEmployees";
//	}
	
//	@RequestMapping(value = "/job/{jobId}/employees/rate", method = RequestMethod.GET)
//	public String getRateEmployeesView(@PathVariable(value = "jobId") int jobId, Model model) {
//		
////		if(markComplete){
////			jobService.markJobComplete(jobId);
////		}
//		
//			jobService.setModel_RateEmployees(model, jobId);
//		
//		return "RateEmployees";
//	}	

//	@RequestMapping(value = "/job/{jobId}/rateEmployees", method = RequestMethod.GET)
//	public ModelAndView viewRateEmployees(@PathVariable(value = "jobId") int jobId, ModelAndView model) {
//
//		List<JobSearchUser> employees = userService.getEmployeesByJob(jobId);
//		model.addObject("employees", employees);
//
//		Job job = jobService.getEmployersJobProfile(jobId);
//		model.addObject("job", job);
//
//		model.setViewName("RateEmployees");
//		return model;
//	}

}