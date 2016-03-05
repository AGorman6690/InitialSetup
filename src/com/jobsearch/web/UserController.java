package com.jobsearch.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Profile;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
@SessionAttributes({ "user" })
public class UserController {


	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	CategoryServiceImpl categoryService;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome(ModelAndView model) {

		// Set session objects
		JobSearchUser user = new JobSearchUser();

		model.addObject("user", user);

		model.setViewName("welcome");

		return model;
	}
	
	@RequestMapping(value = "/findEmployees", method = RequestMethod.GET)
	public ModelAndView findEmployees(ModelAndView model) {
		model.setViewName("FindEmployees");
		return model;
	}
	
	
	@RequestMapping(value = "/findJobs", method = RequestMethod.GET)
	public ModelAndView findJobs(ModelAndView model) {
		model.setViewName("FindJobs");
		return model;
	}
	
	@RequestMapping(value = "/editProfileCategories", method = RequestMethod.GET)
	public ModelAndView editProfileCategories(ModelAndView model) {
		model.setViewName("EditProfileCategories");
		return model;
	}
	
	@RequestMapping(value = "/viewRatings", method = RequestMethod.GET)
	public ModelAndView viewRatings(ModelAndView model) {
		model.setViewName("Ratings");
		return model;
	}
	
	@RequestMapping(value = "/viewApplicationsR", method = RequestMethod.GET)
	public ModelAndView viewApplications(ModelAndView model) {
		model.setViewName("R_Applications");
		return model;
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ModelAndView test(ModelAndView model) {
		model.setViewName("Test");
		return model;
	}
	
	@RequestMapping(value = "/viewApplicationsE", method = RequestMethod.GET)
	public ModelAndView viewApplicationsE(ModelAndView model) {
		model.setViewName("E_Applications");
		return model;
	}
	

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ModelAndView welcomeAgain(HttpServletRequest request, ModelAndView model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		JobSearchUser user = userService.getUserByEmail(auth.getName());
//		request.getSession().setAttribute("user", user);
		model.addObject("user", user);

		return model;
	}
	
	@RequestMapping(value = "/postJob", method = RequestMethod.GET)
	public ModelAndView postJob(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		model.setViewName("PostJob");
		return model;
	}

	@RequestMapping(value = "/signIn", method = RequestMethod.GET)
	public ModelAndView signIn(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		model.setViewName("SignIn");
		return model;
	}
	

	@RequestMapping(value = "/getProfile", method = RequestMethod.GET)
	public ModelAndView getProfile(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		//From the email provided, set the user object
		user = userService.getUserByEmail(user.getEmailAddress());


//		// Set the user's profile object
//		user.setProfile(userService.getProfile(user.getProfileId()));

		model.addObject("user", user);

		// Set the view to return
		if (user.getProfileId() == 2)
			model.setViewName("ProfileYee");
		else
			model.setViewName("ProfileYer");

		return model;
	}

	@RequestMapping(value = "/createUser", method = RequestMethod.GET)
	public ModelAndView createUser(HttpServletRequest request, ModelAndView model,
			@ModelAttribute("user") JobSearchUser user) {

		List<Profile> profiles = userService.getProfiles();

		model.addObject("profiles", profiles);
		model.setViewName("RegisterUser");
		return model;
	}

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ModelAndView registerUser(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		user = userService.createUser(user);

		// Apparently the user object needs to be re-added to the model in order
		// for the view
		// to see the changes to the user object...
		model.addObject("user", user);
		model.setViewName("RegisterCategories");
		return model;
	}
	
	@RequestMapping(value = "/getApplicants", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicants(@RequestParam int jobId) {
		return JSON.stringify(userService.getApplicants(jobId));
	}
	
	
	@RequestMapping(value = "/getOfferedApplicantsByJob", method = RequestMethod.GET)
	@ResponseBody
	public String getOfferedApplicantsByJob(@RequestParam int jobId) {
	//This will return all users who have been extended an offer for a particular job
		return JSON.stringify(userService.getOfferedApplicantsByJob(jobId));
	}
	
	@RequestMapping(value = "/getEmployeesByJob", method = RequestMethod.GET)
	@ResponseBody
	public String getEmployeesByJob(@RequestParam int jobId) {
		return JSON.stringify(userService.getEmployeesByJob(jobId));
	}

	@RequestMapping(value="/getAppRateCriteria", method = RequestMethod.GET)
	@ResponseBody
	public String getAppRateCriteria(){
		return JSON.stringify(userService.getAppRateCriteria());
	}
	
	
	@RequestMapping(value = "/rateEmployee", method = RequestMethod.GET)
	@ResponseBody
	public String rateEmployee(ModelAndView model, 
										@RequestParam int rateCriterionId,
										@RequestParam int value,			
										@RequestParam int employeeId,
										@RequestParam int jobId){

		
		userService.rateEmployee(rateCriterionId, employeeId, jobId, value);
		
		return JSON.stringify("");//JSON.stringify(item);
		
	}
	

	@RequestMapping(value = "/hireApplicant", method = RequestMethod.GET)
	@ResponseBody
	public String hireApplicant(@RequestParam int userId, @RequestParam int jobId) {

		// Add employment to the database
		userService.hireApplicant(userId, jobId);

		return JSON.stringify(userService.getEmployeesByJob(jobId));
	}
	
	//May want to put this in an Application controller
	//******************************************************************************************
	@RequestMapping(value = "/getApplicationsByEmployer", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicationsByEmployer(@RequestParam int userId){
		return JSON.stringify(userService.getApplicationsByEmployer(userId));	
	}
	
	@RequestMapping(value = "/getApplicationsByJob", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicationsByJob(@RequestParam int jobId){
		return JSON.stringify(userService.getApplicationsByJob(jobId));	
	}
	
	@RequestMapping(value = "/markApplicationViewed", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public void markApplicationViewed(@RequestParam int jobId, @RequestParam int userId) {
		
		// Update database
		userService.markApplicationViewed(jobId, userId);
		
	}
	
	@RequestMapping(value = "/markApplicationAccepted", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public String markApplicationAccepted(@RequestParam int jobId, @RequestParam int userId) {
		
		//Update database
		userService.markApplicationAccepted(jobId, userId);		
		userService.hireApplicant(userId, jobId);
		
		return JSON.stringify(jobService.getEmploymentByUser(userId));
		
	}
	//******************************************************************************************

}
