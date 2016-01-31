package com.jobsearch.web;

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
import com.jobsearch.model.Item;
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
	
	@RequestMapping(value = "/viewRatings", method = RequestMethod.GET)
	public ModelAndView viewRatings(ModelAndView model) {
		model.setViewName("Ratings");
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


}
