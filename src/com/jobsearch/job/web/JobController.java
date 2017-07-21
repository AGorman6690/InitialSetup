package com.jobsearch.job.web;

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

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Question;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
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
	@RequestMapping(value = "/job/post", method = RequestMethod.POST)
	public String postJobs(@RequestBody JobDTO jobDto, HttpSession session) {

		jobService.addPosting(jobDto, session);
		return "";
		// return "redirect:/user/profile";

	}
	
	@RequestMapping(value = "/job/{jobId}/application-progress", method = RequestMethod.GET)
	public String getApplicationProgress(@PathVariable(value = "jobId") int jobId,
			Model model, HttpSession session) {

		jobService.setModel_applicationProgress(jobId, model, session);

		return "homepage_employer/ApplicationProgress";
	}

	@ResponseBody
	@RequestMapping(value = "/job/{jobId}/work-days", method = RequestMethod.GET)
	public String getWorkDayDtos_jobInfo(@PathVariable(value = "jobId") int jobId, HttpSession session) {

		List<WorkDayDto> workDayDtos = jobService.getWorkDayDtos(jobId);

		return JSON.stringify(workDayDtos);
	}
	
	@RequestMapping(value = "/job/{jobId}/leave", method = RequestMethod.GET)
	public String leaveJob_employee(@PathVariable(value = "jobId") int jobId,
			HttpSession session) {
		
		jobService.leaveJob_employee(session, jobId);
		return "redirect:/user/profile";
		
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
	@RequestMapping(value = "/preview/job-info", method = RequestMethod.POST)
	public String previewJobInfo(Model model, @RequestBody JobDTO jobDto) {

		jobService.setModel_PreviewJobPost(model, jobDto);
		model.addAttribute("isPreviewingJobPost", true);
		return "/JobInfo_NEW";
	}

	@RequestMapping(value = "/validate-address")
	@ResponseBody
	public String isAddressValid(@RequestParam(name = "address") String address){		
		if(GoogleClient.isValidAddress(address)) return "isValid";
		else return "isNotValid";		
	}
	
	@RequestMapping(value = "/preview/job-info/{jobId}", method = RequestMethod.POST)
	public String previewJobInfo_ByJobId(Model model, HttpSession session,
				@PathVariable(value = "jobId") int jobId,
				@RequestParam(name = "c", required = true) String c,
				@RequestParam(name = "p", required = false) Integer p) {

		// *************************************************
		// *************************************************
		// Refactor all of this
		// *************************************************		
		// *************************************************
		
		
		
//		JobDTO jobDto = jobService.getJobDTO_DisplayJobInfo(jobId);
		JobSearchUser sessionUser = SessionContext.getUser(session);
		if(sessionUser == null){
			if(p == 1) jobService.setModel_ViewJob_Employee(model, session, c, jobId);
			else jobService.setModel_ViewJob_Employer(model, session, c, jobId, "");
			model.addAttribute("isLoggedIn", false);
		}
		else if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE){
			jobService.setModel_ViewJob_Employee(model, session, c, jobId);	
		}else{
			jobService.setModel_ViewJob_Employer(model, session, c, jobId, "");
		}
		
		
//		model.addAttribute("jobDto", jobDto);
//		model.addAttribute("json_work_day_dtos", JSON.stringify(jobDto.getWorkDayDtos()));
		return "/JobInfo_NEW";
	}

	@RequestMapping(value = "/jobs/find", method = RequestMethod.GET)
	public String viewFindJobs(Model model, HttpSession session) {
		
		
//		List<Integer> jobIds_previouslyLoaded = SessionContext.getFilteredJobIds(session);
//		FindJobFilterDTO lastFilterRequest = SessionContext.getLastFilterRequest(session);
//		List<JobDTO> jobDTOs = jobService.setModel_FindJobs_PageLoad(model, session,
//				jobIds_previouslyLoaded, lastFilterRequest);
//
//		double maxDistance = jobService.getMaxDistanceJobFromFilterRequest(jobDTOs);
//		model.addAttribute("filterDto", lastFilterRequest);
//		model.addAttribute("maxDistance", maxDistance);
//		model.addAttribute("jobDtos", jobDTOs);
//		
		return "/find_jobs_new/Find_Jobs_New";
		
	}

	@RequestMapping(value = "/jobs/find/load-filter", method = RequestMethod.GET)
	public String loadFindJobFilter(@RequestParam(name = "savedFindJobFilterId") int savedFindJobFilterId, Model model,
			HttpSession session) {

		jobService.setModel_LoadFindJobsFilter(savedFindJobFilterId, model, session);


		return "/find_jobs/Filters";

	}

	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.GET)
	public String getJob(Model model, HttpSession session, @RequestParam(name = "c", required = true) String c,
			@RequestParam(name = "p", required = false) Integer p, @RequestParam(name = "d", required = false) String d,
			@PathVariable(value = "jobId") int jobId) {

		// c is the context in which the job was clicked.
		// ************************************************************
		// ************************************************************
		// (Phase this out. Use the Session User's profile id. No need to pass
		// it from the browser.)
		// p is the user's profile id.
		// ************************************************************
		// ************************************************************
		// d is whether the **employer** clicked a data point for the job on his
		// profile dashboard
		// (i.e. wage negotiations, applicants, or employees).
		// For the possible values for the "d" variable, see the initPage()
		// function in the View_Job_Employer.jsp file

		JobSearchUser sessionUser = SessionContext.getUser(session);
		if (sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE) {
			jobService.setModel_ViewJob_Employee(model, session, c, jobId);
			return "/view_job_employee/ViewJob_Employee";
		} else if (sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER) {
			jobService.setModel_ViewJob_Employer(model, session, c, jobId, d);
			return "/view_job_employer/ViewJob_Employer";
		} else {
			return SessionContext.get404Page();
		}

	}

	@RequestMapping(value = "job/{jobId}/find-employees", method = RequestMethod.GET)
	public String findEmployees_byJob(Model model, HttpSession session, @PathVariable(value = "jobId") int jobId) {

		jobService.setModel_findEmployees_byJob(model, session, jobId);

		return "/find_employees/FindEmployees";

	}

	@ResponseBody
	@RequestMapping(value = "get/job-dto/{jobId}", method = RequestMethod.GET)
	public String getJobDto(HttpSession session, @PathVariable(value = "jobId") int jobId) {

		JobDTO jobDto = jobService.getJobDto_ByEmployer(session, jobId);

		return JSON.stringify(jobDto);

	}

	@RequestMapping(value = "/job/{jobId}/update/status/{status}", method = RequestMethod.GET)
	public String updateJobStatus(@PathVariable(value = "status") int status,
			@PathVariable(value = "jobId") int jobId) {

		jobService.updateJobStatus(status, jobId);

		return "redirect:/user/profile";
	}

	@ResponseBody
	@RequestMapping(value = "/post-job/previous-question/load", method = RequestMethod.GET)
	public String loadPreviousPostedQuestion(@RequestParam(name = "questionId", required = true) int questionId,
			HttpSession session) {

		// *****************************************************
		// *****************************************************
		// Can the getJobDto methond be used in place of this????
		// The getJobDTO_DisplayJobInfo() should also be responsible
		// for setting the questions.
		// This is redundant.
		// *****************************************************
		// *****************************************************

		Question postedQuestion = jobService.getQuestion_PreviousPostedQuestion(session, questionId);

		return JSON.stringify(postedQuestion);
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

	@RequestMapping(value = "/job/{jobId}/rate-employer", method = RequestMethod.GET)
	public String viewRateEmployer(@PathVariable(value = "jobId") int jobId, Model model, HttpSession session) {

		if (jobService.setModel_ViewRateEmployer(jobId, model, session))
			return "/ratings/RateEmployer";
		else
			return SessionContext.get404Page();
	}

	@RequestMapping(value = "/job/{jobId}/rate-employees", method = RequestMethod.GET)
	public String viewRateEmployees(@PathVariable(value = "jobId") int jobId, Model model, HttpSession session) {

		if (jobService.setModel_ViewRateEmployees(jobId, model, session))
			return "/ratings/RateEmployees";
		else
			return SessionContext.get404Page();
	}

	@RequestMapping(value = "/job/{jobId}/edit", method = RequestMethod.GET)
	public String viewEditJob(Model model, HttpSession session, @PathVariable(value = "jobId") int jobId) {

		if (jobService.setModel_viewReplaceEmployees(model, session, jobId))
			return "/edit_job/Edit_Job";
		else
			return SessionContext.get404Page();
	}

	@RequestMapping(value = "/job/{jobId}/user/{userId}/remove-remaining-work-days", method = RequestMethod.POST)
	@ResponseBody
	public String editJob_removeRemainingWorkDays_forUser(@PathVariable(value = "jobId") int jobId,
			@PathVariable(value = "userId") int userId, HttpSession session) {

		jobService.editJob_removeRemainingWorkDays_forUser(jobId, userId, session);
		return "";
	}

	@ResponseBody
	@RequestMapping(value = "/job/{jobId}/employee/{userId}/work-days", method = RequestMethod.GET)
	public String getWorkDayDtos_proposedWorkDays(@PathVariable(value = "jobId") int jobId,
			@PathVariable(value = "userId") int userId, HttpSession session) {

		return JSON.stringify(applicationService.getWorkDayDtos_proposedWorkDays(jobId, userId, session));
	}
	
//	@RequestMapping(value = "/job/{jobId}/replace-employee/{userId}", method = RequestMethod.POST)
//	@ResponseBody
//	public String replaceEmployee(HttpSession session,
//									@PathVariable(value = "jobId") int jobId,
//									@PathVariable(value = "userId") int userId){	
//
//		jobService.replaceEmployee(session, jobId, userId);
//		return "";
//
//	}
		
	@RequestMapping(value = "/job/{jobId}/edit/work-days", method = RequestMethod.POST)
	@ResponseBody
	public String editJob_workDays(HttpSession session,
									@PathVariable(value = "jobId") int jobId,
									@RequestBody List<WorkDay> workDays){	

		jobService.editJob_workDays(session, workDays, jobId);
		return "";

	}
	
	@RequestMapping(value = "/job/{jobId}/employee/{userId}/display-termination-message", method = RequestMethod.POST)
	public String displayMessage_terminateEmployee(@PathVariable(value = "jobId") int jobId,
													@PathVariable(value = "userId") int userId_emloyee,
													HttpSession session, Model model){
		
		jobService.setModel_displayMessage_terminmateEmployee(model, session, jobId, userId_emloyee);
		return "/terminate_employment/Employer_terminates_employee";
	}
	

	// @RequestMapping(value = "/job/{jobId}/edit/work-days/pre-process", method
	// = RequestMethod.POST)
	// public String edotditJob(Model model, HttpSession session,
	// @PathVariable(value = "jobId") int jobId,
	// @RequestBody List<String> dateStrings_remove) {
	//
	// if(jobService.setModel_editJob_removeWorkDays_preProcess(model, session,
	// jobId, dateStrings_remove))
	// return "/edit_job/RemoveWorkDays_AffectedEmployees";
	//
	// else return SessionContext.get404Page();
	// }

}