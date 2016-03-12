package com.jobsearch.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.jobsearch.model.RateCriterion;
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

		model.addObject("user", user);

		model.setViewName("Welcome");

		return model;
	}

	@RequestMapping(value = "/viewFindEmployees", method = RequestMethod.GET)
	public ModelAndView viewFindEmployees(ModelAndView model) {
		model.setViewName("FindEmployees");
		return model;
	}

	@RequestMapping(value = "/rateEmployees", method = RequestMethod.GET)
	public ModelAndView rateEmployees(ModelAndView model) {
		model.setViewName("RateEmployees");
		return model;
	}

	@RequestMapping(value = "/viewFindJobs", method = RequestMethod.GET)
	public ModelAndView viewFindJobs(ModelAndView model) {
		model.setViewName("FindJobs");
		return model;
	}

	@RequestMapping(value = "/viewEditProfileCategories", method = RequestMethod.GET)
	public ModelAndView viewEditProfileCategories(ModelAndView model) {
		model.setViewName("EditProfileCategories");
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

	@RequestMapping(value = "/signIn", method = RequestMethod.GET)
	public ModelAndView signIn(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		model.setViewName("SignIn");
		return model;
	}

	@RequestMapping(value = "/getProfile", method = RequestMethod.GET)
	public ModelAndView getProfile(ModelAndView model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		JobSearchUser user = userService.getUserByEmail(auth.getName());

		model.addObject("user", user);

		if (user.getProfileId() == 2)
			model.setViewName("EmployeeProfile");
		else
			model.setViewName("EmployerProfile");

		return model;
	}

	@RequestMapping(value = "/user/{userId}/categories", method = RequestMethod.GET)
	@ResponseBody
	public String getCategoriesByUser(@PathVariable int userId) {
		return JSON.stringify(categoryService.getCategoriesByUserId(userId));
	}

	@RequestMapping(value = "/user/{userId}/removeCategories", method = RequestMethod.PUT)
	@ResponseBody
	public String removeCategories(@PathVariable int userId,
			@RequestParam(required = true) List<Integer> category) {

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

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ModelAndView registerUser(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		user = userService.createUser(user);
		model.addObject("user", user);

		if (user.getProfileId() == 2)
			model.setViewName("EmployeeProfile");
		else
			model.setViewName("EmployerProfile");

		return model;
	}

	@RequestMapping(value = "/getApplicants", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicants(@RequestParam int jobId) {

		List<JobSearchUser> applicants = userService.getApplicants(jobId);

		// //This is the rate criteria for the application. Rate Criterion
		// should have its own controller.
		// List<RateCriterion> ratingCriteria = userService.getRatingCriteia();

		for (JobSearchUser applicant : applicants) {
			applicant.setRatings(userService.getRatingCriteia());
			applicant.setRatings(userService.getRatings(applicant.getUserId(), applicant.getRatings()));
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
		return JSON.stringify(userService.getEmployeesByJob(jobId));
	}

	@RequestMapping(value = "/user/rate", method = RequestMethod.POST)
	@ResponseBody
	public void rateEmployee(ModelAndView model, @RequestBody RatingDTO ratingDto) {

		userService.rateEmployee(ratingDto);
	}

	@RequestMapping(value = "/user/hire", method = RequestMethod.GET)
	@ResponseBody
	public String hireApplicant(@RequestParam int userId, @RequestParam int jobId) {

		// Add employment to the database
		userService.hireApplicant(userId, jobId);

		return JSON.stringify(userService.getEmployeesByJob(jobId));
	}

	@RequestMapping(value = "/user/{userId}/employment", method = RequestMethod.GET)
	@ResponseBody
	public String getEmploymentByUser(@PathVariable int userId) {
		return JSON.stringify(jobService.getEmploymentByUser(userId));
	}
}
