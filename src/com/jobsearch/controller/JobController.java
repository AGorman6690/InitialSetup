package com.jobsearch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.model.Job;
import com.jobsearch.job.web.FindJobFilterDTO;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Question;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.request.AddJobRequest;
import com.jobsearch.request.EditJobRequest;
import com.jobsearch.responses.ValidateAddressResponse;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.session.SessionContext;

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
	@RequestMapping(value = "/job", method = RequestMethod.POST)
	public String postJobs(@RequestBody AddJobRequest request, HttpSession session) {
		jobService.addJob(request, session);
		return "";
	}
	
	@RequestMapping(value = "/job/{jobId}/application-progress", method = RequestMethod.GET)
	public String getApplicationProgressRequest(@PathVariable(value = "jobId") int jobId,
			Model model, HttpSession session) {

		jobService.setApplicationProgressResponse(jobId, model, session);
		return "homepage_employer/ApplicationProgress";
	}
	
	@RequestMapping(value = "/job/{jobId}/leave", method = RequestMethod.GET)
	public String leaveJob_employee(@PathVariable(value = "jobId") int jobId,
			HttpSession session) {
		
		jobService.leaveJob_employee(session, jobId);
		return "redirect:/user";
		
	}

	@RequestMapping(value = "/jobs/filtered/sort", method = RequestMethod.GET)
	public String getSortedJobs(@RequestParam(name = "sortBy") String sortBy,
			@RequestParam(name = "isAscending") boolean isAscending, HttpSession session, Model model) {

		jobService.setModel_SortFilteredJobs(session, model, sortBy, isAscending);

		return "/find_jobs/Render_GetJobs_InitialRequest";
	}

	@RequestMapping(value = "/jobs/save-find-job-filter", method = RequestMethod.POST)
	@ResponseBody
	public String saveFindJobFilter(@RequestBody FindJobFilterDTO savedFilter, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			jobService.saveFindJobFilter(savedFilter, session);
		}
		return "";
	}
	
	

	@RequestMapping(value = "/jobs/filter", method = RequestMethod.GET)
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
			@RequestParam(name = "doMatchAllDays", value = "doMatchAllDays", required = false) boolean doMatchAllDays,
			@RequestParam(name = "duration", required = false) Double duration,
			@RequestParam(name = "isShorterThanDuration", required = false) boolean isShorterThanDuration,
			@RequestParam(name = "returnJobCount", required = false, defaultValue = "25") Integer returnJobCount,
			@RequestParam(name = "sortBy", required = false) String sortBy,
			@RequestParam(name = "isAscending", required = false) boolean isAscending,
			@RequestParam(name = "isAppendingJobs", required = true) boolean isAppendingJobs,
			@RequestParam(name = "savedName", required = false) String savedName,
			// @RequestParam(value = "id", required = false) int[] loadedJobIds
			// ,
			HttpSession session, Model model) {
		// *********************************************************
		// *********************************************************
		// look into making this a POST method and pass a dto object like
		// /save-find-job-filter.
		// then then client side code for "gathering the filter data" can
		// be shared between the get jobs (this end point)
		// and save filter action
		// *********************************************************
		// *********************************************************

		String page = StringUtils.EMPTY;

		List<JobDTO> jobDTOs = new ArrayList<>(25);
		FindJobFilterDTO filter = new FindJobFilterDTO(radius, fromAddress, categoryIds, startTime, endTime,
				beforeStartTime, beforeEndTime, startDate, endDate, beforeStartDate, beforeEndDate, workingDays,
				doMatchAllDays, duration, isShorterThanDuration, returnJobCount, sortBy, isAscending, isAppendingJobs,
				city, state, zipCode, savedName);

		if (filter.getIsAppendingJobs()) {
			filter.setJobIdsToExclude(SessionContext.getFilteredJobIds(session));
			jobDTOs = jobService.getMoreJobs(model, filter);
			List<Integer> jobIds = jobDTOs.stream().map(JobDTO::getJob).map(Job::getId).collect(Collectors.toList());

			SessionContext.appendToFilteredJobIds(session, jobIds);
//			if(jobDTOs.size() == 0) page = "/find_jobs/no-more-jobs"
			page = "/find_jobs/Render_FilteredJobsList";
		} else {
			SessionContext.setFilteredJobIds(session, null);
			filter.setJobIdsToExclude(null);
			jobDTOs = jobService.getFilteredJobs(model, filter);

			double maxDistance = jobService.getMaxDistanceJobFromFilterRequest(jobDTOs);
			model.addAttribute("filterDto", filter);
			model.addAttribute("maxDistance", maxDistance);

			List<Integer> jobIds = jobDTOs.stream().map(JobDTO::getJob).map(Job::getId).collect(Collectors.toList());

			// Update the session variables
			SessionContext.setLastFilterRequest(session, filter);
			SessionContext.setFilteredJobIds(session, jobIds);

			page = "/find_jobs/Render_GetJobs_InitialRequest";
		}
		model.addAttribute("jobDtos", jobDTOs);

		return page;

	}

	@RequestMapping(value = "/job/{jobId}/work-day/{dateString}/applicants", method = RequestMethod.GET)
	public String getApplicants_byJobAndDate(Model model, HttpSession session, @PathVariable(value = "jobId") int jobId,
			@PathVariable(value = "dateString") String dateString) {
		jobService.setModel_getApplicants_byJobAndDate(model, session, jobId, dateString);
		return "/view_job_employer/ApplicationSummary_ByWorkDay";
	}

	@RequestMapping(value = "/job/{jobId}/employees/by-work-days", method = RequestMethod.POST)
	public String getEmployees_byJobAndDates(Model model, HttpSession session, @PathVariable(value = "jobId") int jobId,
			@RequestBody List<String> dateStrings) {
		jobService.setModel_getEmployees_byJobAndDate(model, session, jobId, dateStrings);
		return "/edit_job/Employees_ByDate";
	}

	@RequestMapping(value = "/job/validate-address")
	@ResponseBody
	public String isAddressValid(@RequestParam(name = "address") String address){
		// Should a GoogleController exist?????
		ValidateAddressResponse response = jobService.getValidateAddressResponse(address);
		return JSON.stringify(response);
	}
	
	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.POST)
	public String getJob(Model model, HttpSession session,
				@PathVariable(value = "jobId") int jobId,
				@RequestParam(name = "c", required = true) String c,
				@RequestParam(name = "p", required = false) Integer p) {

		jobService.setGetJobResponse(model, session, c, jobId);	
		return "/JobInfo_NEW";
	}
	
	@RequestMapping(value = "/job/preview", method = RequestMethod.POST)
	public String previewJobInfo(Model model, @RequestBody AddJobRequest request) {

		jobService.setGetJobReponse_forPreviewingJobPost(model, request);
		
		return "/JobInfo_NEW";
	}

	@RequestMapping(value = "/jobs/find", method = RequestMethod.GET)
	public String viewFindJobs(Model model, HttpSession session) {
		return "/find_jobs_new/Find_Jobs_New";		
	}

	@RequestMapping(value = "/jobs/find/load-filter", method = RequestMethod.GET)
	public String loadFindJobFilter(@RequestParam(name = "savedFindJobFilterId") int savedFindJobFilterId, Model model,
			HttpSession session) {
		jobService.setModel_LoadFindJobsFilter(savedFindJobFilterId, model, session);
		return "/find_jobs/Filters";
	}

	@RequestMapping(value = "job/{jobId}/find-employees", method = RequestMethod.GET)
	public String findEmployees_byJob(Model model, HttpSession session, @PathVariable(value = "jobId") int jobId) {

		jobService.setModel_findEmployees_byJob(model, session, jobId);
		return "/find_employees/FindEmployees";
	}

	@RequestMapping(value = "/job/{jobId}/update/status/{status}", method = RequestMethod.GET)
	public String updateJobStatus(@PathVariable(value = "status") int status,
			@PathVariable(value = "jobId") int jobId) {

		jobService.updateJobStatus(status, jobId);
		return "redirect:/user";
	}

	@ResponseBody
	@RequestMapping(value = "/post-job/previous-post/load", method = RequestMethod.GET)
	public String loadPreviousJobPost(@RequestParam(name = "jobId", required = true) int jobId, HttpSession session) {

		JobDTO jobDto = jobService.getJobDto_PreviousPostedJob(session, jobId);

		return JSON.stringify(jobDto);
	}

	@RequestMapping(value = "/post-job", method = RequestMethod.GET)
	public String viewPostJob(Model model, HttpSession session) {

		jobService.setModel_ViewPostJob(model, session);

		return "/post_job/PostJob";
	}


	@RequestMapping(value = "/job/{jobId}/edit", method = RequestMethod.GET)
	public String viewEditJob(Model model, HttpSession session, @PathVariable(value = "jobId") int jobId) {

		if (jobService.setModel_viewReplaceEmployees(model, session, jobId))
			return "/edit_job/Edit_Job";
		else
			return SessionContext.get404Page();
	}
	
	@RequestMapping(value = "/job/edit", method = RequestMethod.POST)
	@ResponseBody
	public String editJob(HttpSession session, @RequestBody EditJobRequest request){
		jobService.editJobRequest(session, request);
		return "";
	}

	@RequestMapping(value = "/job/{jobId}/user/{userId}/remove-remaining-work-days", method = RequestMethod.POST)
	@ResponseBody
	public String editJob_removeRemainingWorkDays_forUser(@PathVariable(value = "jobId") int jobId,
			@PathVariable(value = "userId") int userId, HttpSession session) {

		jobService.editJob_removeRemainingWorkDays_forUser(jobId, userId, session);
		return "";
	}


	

	
	@RequestMapping(value = "/job/{jobId}/employee/{userId}/display-termination-message", method = RequestMethod.POST)
	public String displayMessage_terminateEmployee(@PathVariable(value = "jobId") int jobId,
													@PathVariable(value = "userId") int userId_emloyee,
													HttpSession session, Model model){
		
		jobService.setModel_displayMessage_terminmateEmployee(model, session, jobId, userId_emloyee);
		return "/terminate_employment/Employer_terminates_employee";
	}
	

}