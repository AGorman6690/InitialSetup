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

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.FindJobFilterDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.job.service.SubmitJobPostingRequestDTO;
import com.jobsearch.job.service.test;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
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
	public void postJobs(@RequestBody SubmitJobPostingRequestDTO postingDto, HttpSession session, ModelAndView model) {

		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		jobService.addPosting(postingDto, user);
	}

	@RequestMapping(value = "/jobs/filtered/sort", method = RequestMethod.GET)
	public String getSortedJobs(@RequestParam(name = "sortBy") String sortBy,
			@RequestParam(name = "isAscending") boolean isAscending, HttpSession session, Model model) {
		
		jobService.SetModel_SortFilteredJobs(session, model, sortBy, isAscending);

		// Get the html to display the already-rendered jobs in the requested
		// order
		return "/find_jobs/RenderFilteredJobs";
	}
	
	@RequestMapping(value = "/jobs/save-find-job-filter", method = RequestMethod.POST)
	@ResponseBody
//	public void saveFindJobFilter(@RequestParam(name = "radius", required = true) int radius,
//			@RequestParam(name = "fromAddress", required = true) String fromAddress,
//			@RequestParam(name = "city", required = false) String city,
//			@RequestParam(name = "state", required = false) String state,
//			@RequestParam(name = "zipCode", required = false) String zipCode,
//			@RequestParam(name = "categoryId", value = "categoryId", required = false) int[] categoryIds,
//			@RequestParam(name = "startTime", required = false) String startTime,
//			@RequestParam(name = "endTime", required = false) String endTime,
//			@RequestParam(name = "beforeStartTime", required = false) boolean beforeStartTime,
//			@RequestParam(name = "beforeEndTime", required = false) boolean beforeEndTime,
//			@RequestParam(name = "startDate", required = false) String startDate,
//			@RequestParam(name = "endDate", required = false) String endDate,
//			@RequestParam(name = "beforeStartDate", required = false) boolean beforeStartDate,
//			@RequestParam(name = "beforeEndDate", required = false) boolean beforeEndDate,
//			@RequestParam(name = "d", value = "d", required = false) List<String> workingDays,
//			@RequestParam(name = "duration", required = false) Double duration,
//			@RequestParam(name = "isLessThanDuration", required = false) boolean isLessThanDuration,
//			@RequestParam(name = "returnJobCount", required = false, defaultValue = "25") Integer returnJobCount,
//			@RequestParam(name = "sortBy", required = false) String sortBy,
//			@RequestParam(name = "isAscending", required = false) boolean isAscending,
//			@RequestParam(name = "isAppendingJobs", required = true) boolean isAppendingJobs,
//			@RequestParam(name = "dt", value="dt", required = false) Integer[] durationTypeIds,
//			@RequestParam(name = "savedName", required = true) String savedName,
//			HttpSession session
//			){
	public String saveFindJobFilter(@RequestBody FindJobFilterDTO savedFilter,
			HttpSession session
			){	

		if(SessionContext.isLoggedIn(session)){
//			FindJobFilterDTO savedFilter = new FindJobFilterDTO(radius, fromAddress, categoryIds, startTime, endTime, beforeStartTime,
//					beforeEndTime, startDate, endDate, beforeStartDate, beforeEndDate, workingDays, duration,
//					isLessThanDuration, returnJobCount, sortBy, isAscending, isAppendingJobs, durationTypeIds,
//					city, state, zipCode, savedName);
	//				
				jobService.saveFindJobFilter(savedFilter, session);
		}	
		
		return "";
	}


	@RequestMapping(value = "/jobs/filter", method = RequestMethod.GET)
//	@ResponseBody
	public String getFilteredJobs(@RequestParam(name = "radius", required = true) int radius,
			@RequestParam(name = "fromAddress", required = true) String fromAddress,
			@RequestParam(name = "city", required = false) String city,
			@RequestParam(name = "state", required = false) String state,
			@RequestParam(name = "zipCode", required = false) String zipCode,
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
			@RequestParam(name = "duration", required = false) Double duration,
			@RequestParam(name = "isLessThanDuration", required = false) boolean isLessThanDuration,
			@RequestParam(name = "returnJobCount", required = false, defaultValue = "25") Integer returnJobCount,
			@RequestParam(name = "sortBy", required = false) String sortBy,
			@RequestParam(name = "isAscending", required = false) boolean isAscending,
			@RequestParam(name = "isAppendingJobs", required = true) boolean isAppendingJobs,
			@RequestParam(name = "dt", value="dt", required = false) Integer[] durationTypeIds,
			@RequestParam(name = "savedName", required = false) String savedName,
//			@RequestParam(value = "id", required = false) int[] loadedJobIds ,
			HttpSession session,
			Model model
			){

		FindJobFilterDTO request = new FindJobFilterDTO(radius, fromAddress, categoryIds, startTime, endTime, beforeStartTime,
				beforeEndTime, startDate, endDate, beforeStartDate, beforeEndDate, workingDays, duration,
				isLessThanDuration, returnJobCount, sortBy, isAscending, isAppendingJobs, durationTypeIds,
				city, state, zipCode, savedName);
				
			jobService.setModel_FilterJobs(model, request, session);
			
			return "/find_jobs/RenderFilteredJobs";

	}

	@ResponseBody
	@RequestMapping(value = "/job/apply", method = RequestMethod.POST)
	public String applyForJob(@RequestBody Application application, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			applicationService.applyForJob(application, session);
			return "redirect:/user/profile";
		} else {
			return "NotLoggedIn";
		}

	}

	@RequestMapping(value = "/jobs/find", method = RequestMethod.GET)
	public String viewFindJobs(Model model, HttpSession session) {

		jobService.setModel_FindJobs_PageLoad(model, session);
		
		return "/find_jobs/FindJobs";
	}
	
	@RequestMapping(value = "/jobs/find/load-filter", method = RequestMethod.GET)
	public String loadFindJobFilter(@RequestParam (name="savedFindJobFilterId") int savedFindJobFilterId,
										Model model, HttpSession session) {

		jobService.setModel_LoadFindJobsFilter(savedFindJobFilterId, model, session);
		
		return "/find_jobs/Filters";
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

		// If employee
		if (user.getProfileId() == 1) {
			jobService.setModel_EmployeeViewJobFromProfile(model, jobId, user.getUserId());
			return "EmployeeViewJobFromProfile";
			// Else if employer
		} else if (user.getProfileId() == 2) {
			jobService.setModel_EmployerViewJob(model, jobId, session);
			return "EmployerViewJob";
		}

		return null;

	}

	
	@RequestMapping(value = "/completed/job/{jobId}", method = RequestMethod.GET)
	public String getCompletedJob(@PathVariable(value = "jobId") int jobId, Model model, HttpSession session) {
		
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

		model.setViewName("EditJob");
		return model;
	}


}