package com.jobsearch.controller;

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

import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.request.AddJobRequest;
import com.jobsearch.request.EditJobRequest;
import com.jobsearch.request.FindJobsRequest;
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

	@RequestMapping(value = "/jobs/find/request", method = RequestMethod.POST)
	public String getFilteredJobs(@RequestBody FindJobsRequest request, HttpSession session, Model model){
			
		jobService.setFindJobsResponse(model, session, request);
		
		if(request.getIsAppendingJobs()){
			return "/find_jobs/Render_FilteredJobsList";	
		}else{
			return "/find_jobs/Render_GetJobs_InitialRequest";
		}
		
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
	public String previewJobInfo(Model model, HttpSession session, @RequestBody AddJobRequest request) {

		
		jobService.setGetJobReponse_forPreviewingJobPost(model, request);
		
		return "/JobInfo_NEW";
	}

	@RequestMapping(value = "/jobs/find", method = RequestMethod.GET)
	public String viewFindJobs(Model model, HttpSession session) {
		
		JobSearchUser user = SessionContext.getUser(session);
		Integer radius = user.getMaxWorkRadius();
		String address = userService.buildAddress(user);
		model.addAttribute("address", address);
		if(address != null && radius != null && radius > 0){
			FindJobsRequest request = new FindJobsRequest();
			request.setAddress(address);
			request.setRadius(radius);			
			jobService.setFindJobsResponse(model, session, request);
		}
		
		return "/FindJobs";		
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