package com.jobsearch.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.CreateJobDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.user.service.JobSearchUser;

@Controller
@SessionAttributes({ "user", "job" })
public class JobController {

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	CategoryServiceImpl categoryService;

	@RequestMapping(value = "/createJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
//	public ModelAndView addJob(@RequestBody CreateJobDTO jobDto, ModelAndView model) {
	public ModelAndView addJob(@ModelAttribute("job") CreateJobDTO jobDto, ModelAndView model) {

		//Add the job to the job table
		List<Job> jobsCreatedByUser = jobService.addJob(jobDto);
	 
		model.setViewName("EmployerProfile");
		//return JSON.stringify(jobsCreatedByUser);
		return model;
	}

	@RequestMapping(value = "/viewActiveJob_Employer", method = RequestMethod.GET)
	public ModelAndView viewActiveJob_Employer(@RequestParam int jobId, ModelAndView model) {
		
		Job activeJob = jobService.getJob(jobId);
		model.addObject("job", JSON.stringify(activeJob));
		//model.addObject("job", activeJob);
		model.setViewName("ViewActiveJob_Employer");
		return model;
	}
	
	@RequestMapping(value = "/job/edit", method = RequestMethod.GET)
	public ModelAndView viewEditJob(ModelAndView model) {
		
//		Job activeJob = jobService.getJob(jobId);
//		model.addObject("job", JSON.stringify(activeJob));

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

	@RequestMapping(value = "/getCompletedJobsByUser", method = RequestMethod.GET)
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


	@RequestMapping(value = "/job/{jobId}/markComplete", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView markJobComplete(@PathVariable("jobId") int jobId, @ModelAttribute("user") JobSearchUser user) {

		// Update database
		jobService.markJobComplete(jobId);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("EmployerProfile");

		return model;
	}
	
//
//	@RequestMapping(value = "/job/{jobID}/category", method = RequestMethod.GET)
//	@ResponseBody
//	public String getCategoryByJob(@PathVariable int jobId){		
//		return JSON.stringify(categoryService.getCategoriesByJobId(jobId));
//	}

}