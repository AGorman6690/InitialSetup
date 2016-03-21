package com.jobsearch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.jobsearch.model.Profile;
import com.jobsearch.user.rate.RatingDTO;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
@SessionAttributes({ "user", "job" })
public class UserController {

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	CategoryServiceImpl categoryService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome(ModelAndView model) {

		// // Set session objects
		JobSearchUser user = new JobSearchUser();
		
		List<Profile> profiles = userService.getProfiles();
		model.addObject("profiles", profiles);
		model.addObject("user", user);

		model.setViewName("Welcome");

		return model;
	}

	@RequestMapping(value = "/validateEmail", method = RequestMethod.GET)
	public ModelAndView validate(@RequestParam int userId, ModelAndView model,
			@ModelAttribute("user") JobSearchUser user) {

		user = userService.validateUser(userId);

		model.addObject("user", user);

		if (user.getProfileId() == 1)
			model.setViewName("EmployeeProfile");
		else
			model.setViewName("EmployerProfile");

		model.addObject("user", user);
		return model;
	}

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ModelAndView registerUser(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		user = userService.createUser(user);
		model.addObject("user", user);
		model.setViewName("EmailValidateMessage");
		return model;
	}

	@RequestMapping(value = "/viewFindEmployees", method = RequestMethod.GET)
	public ModelAndView viewFindEmployees(ModelAndView model) {
		model.setViewName("FindEmployees");
		return model;
	}

	@RequestMapping(value = "/viewFindJobs", method = RequestMethod.GET)
	public ModelAndView viewFindJobs(ModelAndView model) {
		model.setViewName("FindJobs");
		return model;
	}

	@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
	public ModelAndView viewProfile(ModelAndView model) {
		model.setViewName("UserProfile");
		return model;
	}

	@RequestMapping(value = "/viewRatings", method = RequestMethod.GET)
	public ModelAndView viewRatings(ModelAndView model) {
		model.setViewName("Ratings");
		return model;
	}

	@RequestMapping(value = "/viewPostJob", method = RequestMethod.GET)
	public ModelAndView viewPostJob(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		CreateJobDTO job = new CreateJobDTO();
		model.addObject("job", job);

		model.setViewName("PostJob");
		return model;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		model.setViewName("Welcome");

		user = new JobSearchUser();

		model.addObject("user", user);

		List<Profile> profiles = userService.getProfiles();
		model.addObject("profiles", profiles);

		return model;
	}

	@RequestMapping(value = "/getProfile", method = RequestMethod.GET)
	public ModelAndView getProfile(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		if (user.getUserId() == 0) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			user = userService.getUserByEmail(auth.getName());
		}

		List<Job> activeJobs = jobService.getActiveJobsByUser(user.getUserId());
		List<Job> completedJobs = jobService.getCompletedJobsByUser(user.getUserId());

		model.addObject("user", user);

		model.addObject("activeJobs", activeJobs);
		model.addObject("completedJobs", completedJobs);

		if (user.getProfileId() == 1)
			model.setViewName("EmployerProfile");
		else
			model.setViewName("EmployeeProfile");

		return model;
	}

	@RequestMapping(value = "/user/{userId}/categories", method = RequestMethod.GET)
	@ResponseBody
	public String getCategoriesByUser(@PathVariable int userId) {
		return JSON.stringify(categoryService.getCategoriesByUserId(userId));
	}

	@RequestMapping(value = "/user/{userId}/removeCategories", method = RequestMethod.PUT)
	@ResponseBody
	public String removeCategories(@PathVariable int userId, @RequestParam(required = true) List<Integer> category) {

		for (int categoryId : category) {
			categoryService.removeCategoryFromUser(userId, categoryId);
		}

		// Return the categories associated with the user's id
		return JSON.stringify(categoryService.getCategoriesByUserId(userId));
	}

	// THIS RETURNS PROFILE OBJECTS. PERHPAS A NEW CONTROLLER MAKES SENSE.
	// ***************************************************************************
	@RequestMapping(value = "/getProfiles", method = RequestMethod.GET)
	@ResponseBody
	public String getProfiles() {

		List<Profile> profiles = userService.getProfiles();
		return JSON.stringify(profiles);
	}
	// ***************************************************************************

	@RequestMapping(value = "/getApplicants", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicants(@RequestParam int jobId) {

		List<JobSearchUser> applicants = userService.getApplicants(jobId);

		for (JobSearchUser applicant : applicants) {
			applicant.setRatings(userService.getRatings(applicant.getUserId()));
		}

		return JSON.stringify(applicants);
	}

	@RequestMapping(value = "/getOfferedApplicantsByJob", method = RequestMethod.GET)
	@ResponseBody
	public String getOfferedApplicantsByJob(@RequestParam int jobId) {
		// This will return all users who have been extended an offer for a
		// particular job
		return JSON.stringify(userService.getOfferedApplicantsByJob(jobId));
	}

	@RequestMapping(value = "/getEmployeesByJob", method = RequestMethod.GET)
	@ResponseBody
	public String getEmployeesByJob(@RequestParam int jobId) {
		return "";
	}

	@RequestMapping(value = "/user/rate", method = RequestMethod.POST)
	@ResponseBody
	public void rateEmployee(ModelAndView model, @RequestBody RatingDTO ratingDto) {

		userService.rateEmployee(ratingDto);
	}


	@RequestMapping(value = "/user/{userId}/employment", method = RequestMethod.GET)
	@ResponseBody
	public String getEmploymentByUser(@PathVariable int userId) {
		return JSON.stringify(jobService.getEmploymentByUser(userId));
	}
}
