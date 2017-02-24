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
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.FindJobFilterDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Question;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;

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
//		return "redirect:/user/profile";

	}

	@RequestMapping(value = "/jobs/filtered/sort", method = RequestMethod.GET)
	public String getSortedJobs(@RequestParam(name = "sortBy") String sortBy,
			@RequestParam(name = "isAscending") boolean isAscending, HttpSession session, Model model) {
		
		jobService.setModel_SortFilteredJobs(session, model, sortBy, isAscending);

		return "/find_jobs/Render_GetJobs_InitialRequest";
	}
	
	@RequestMapping(value = "/jobs/save-find-job-filter", method = RequestMethod.POST)
	@ResponseBody
	public String saveFindJobFilter(@RequestBody FindJobFilterDTO savedFilter,
											HttpSession session){	

		if(SessionContext.isLoggedIn(session)){
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
			@RequestParam(name = "duration", required = false) Double duration,
			@RequestParam(name = "isShorterThanDuration", required = false) boolean isShorterThanDuration,
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
		// *********************************************************
		// *********************************************************
		// look into making this a POST method and pass a dto object like /save-find-job-filter.
		// then then client side code for "gathering the filter data" can
		// be shared between the get jobs (this end point)
		// and save filter action
		// *********************************************************
		// *********************************************************
		

		FindJobFilterDTO filter = new FindJobFilterDTO(radius, fromAddress, categoryIds, startTime, endTime, beforeStartTime,
				beforeEndTime, startDate, endDate, beforeStartDate, beforeEndDate, workingDays, duration,
				isShorterThanDuration, returnJobCount, sortBy, isAscending, isAppendingJobs, durationTypeIds,
				city, state, zipCode, savedName);
				
			
			if(filter.getIsAppendingJobs()){
				jobService.setModel_GetMoreJobs(model, filter, session);
				return "/find_jobs/Render_FilteredJobsList";
			}
			else{
				jobService.setModel_GetJobs(model, filter, session);
				return "/find_jobs/Render_GetJobs_InitialRequest";
			}

	}
	
	

	@ResponseBody
	@RequestMapping(value = "/job/apply", method = RequestMethod.POST)
	public String applyForJob(@RequestBody ApplicationDTO applicationDto, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			applicationService.applyForJob(applicationDto, session);
			return "redirect:/user/profile";
		} else {
			return "NotLoggedIn";
		}

	}
	
	@RequestMapping(value = "/preview/job-info", method = RequestMethod.POST)
	public String previewJobInfo(Model model, @RequestBody JobDTO jobDto) {

		jobService.setModel_PreviewJobPost(model, jobDto);
		
		return "/templates/JobInformation";
	}
	
	@RequestMapping(value = "/preview/job-info/{jobId}", method = RequestMethod.POST)
	public String previewJobInfo_ByJobId(Model model, @PathVariable(value = "jobId") int jobId) {

		JobDTO jobDto = jobService.getJobDTO_DisplayJobInfo(jobId);
		
		model.addAttribute("jobDto", jobDto);
		
		return "/templates/JobInformation";
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
	
	
	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.GET)
	public String getJob(Model model, HttpSession session,
						@RequestParam(name = "c", required = true) String c,
						@RequestParam(name = "p", required = true) Integer p,
						@RequestParam(name = "d", required = false) String d,
						@PathVariable(value = "jobId") int jobId) {	
		
		// c is the context in which the job was clicked
		// p is the user's profile id
		// data is whether the **employer** clicked a data point for the job
		// (i.e. wage negotiations, applicants, or employees)
		
		if(p == 1){
			jobService.setModel_ViewJob_Employee(model, session, c, jobId);	
			return  "/view_job_employee/ViewJob_Employee";
		}
		else if(p == 2){
			jobService.setModel_ViewJob_Employer(model, session, c, jobId, d);	
			return  "/view_job_employer/ViewJob_Employer";
		}
		else{		
			return SessionContext.get404Page();
		}
			
		
	}

	
	@ResponseBody
	@RequestMapping(value = "get/job-dto/{jobId}", method = RequestMethod.GET)
	public String getJobDto(HttpSession session,
						@PathVariable(value = "jobId") int jobId) {	
		
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

		Question postedQuestion = jobService.getQuestion_PreviousPostedQuestion(session, questionId);
		
		return JSON.stringify(postedQuestion);
	}
	
	@ResponseBody
	@RequestMapping(value = "/post-job/previous-post/load", method = RequestMethod.GET)
	public String loadPreviousJobPost(@RequestParam(name = "jobId", required = true) int jobId,
										HttpSession session) {

		JobDTO jobDto = jobService.getJobDto_PreviousPostedJob(session, jobId);
		
		return JSON.stringify(jobDto);
	}
	
	@RequestMapping(value = "/post-job", method = RequestMethod.GET)
	public String viewPostJob(Model model, HttpSession session) {

		
		jobService.setModel_ViewPostJob(model, session);
		
		return "/post_job/PostJob";
	}
	
	@RequestMapping(value = "/job/{jobId}/rate-employer", method = RequestMethod.GET)
	public String viewRateEmployer(@PathVariable(value= "jobId") int jobId,
									Model model, HttpSession session) {

		if(jobService.setModel_ViewRateEmployer(jobId, model, session)) return "/ratings/RateEmployer";
		else return SessionContext.get404Page();
	}	

	@RequestMapping(value = "/job/edit", method = RequestMethod.GET)
	public ModelAndView viewEditJob(ModelAndView model) {

		model.setViewName("EditJob");
		return model;
	}


}