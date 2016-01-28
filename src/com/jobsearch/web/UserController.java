package com.jobsearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.App;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.model.Item;
import com.jobsearch.model.Profile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({ "user" })
public class UserController {

	// @ModelAttribute
	// public JobSearchUser addUser(){
	// JobSearchUser user = new JobSearchUser();
	// return user;
	// }

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	CategoryServiceImpl categoryService;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome(HttpServletRequest request, ModelAndView model) {

		// Set session objects
		JobSearchUser user = new JobSearchUser();

		model.addObject("user", user);

		model.setViewName("welcome");

		return model;
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ModelAndView welcomeAgain(HttpServletRequest request, ModelAndView model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		JobSearchUser user = userService.getUserByEmail(auth.getName());
		request.getSession().setAttribute("user", user);
		model.addObject("user", user);

		return model;
	}

	@RequestMapping(value = "/signIn", method = RequestMethod.GET)
	public ModelAndView signIn(HttpServletRequest request, ModelAndView model,
			@ModelAttribute("user") JobSearchUser user) {

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
	public ModelAndView registerUser(HttpServletRequest request, ModelAndView model,
			@ModelAttribute("user") JobSearchUser user) {

		user = userService.createUser(user);

		// Apparently the user object needs to be re-added to the model in order
		// for the view
		// to see the changes to the user object...
		model.addObject("user", user);
		model.addObject("categories", categoryService.getCategories());
		model.setViewName("RegisterCategories");
		return model;
	}

	@RequestMapping(value = "deleteCategoryFromUser", method = RequestMethod.GET)
	@ResponseBody
	public String deleteCategoryFromUser(HttpServletRequest request, ModelAndView model, @RequestParam int categoryId,
											@RequestParam int userId) {

		// Update database
		userService.deleteCategory(userId, categoryId);

		return JSON.stringify(categoryService.getCategoriesByUserId(userId));

	}
	
	@RequestMapping(value = "getApplicants", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicants(@RequestParam int jobId) {
		return JSON.stringify(userService.getApplicants(jobId));
	}
	
	@RequestMapping(value = "getEmployees", method = RequestMethod.GET)
	@ResponseBody
	public String getEmployees(@RequestParam int jobId) {
		return JSON.stringify(userService.getEmployees(jobId));
	}

	@RequestMapping(value = "addCategoryToUser", method = RequestMethod.GET)
	@ResponseBody
	public String addCategoryToUser(ModelAndView model, @RequestParam int categoryId, @RequestParam int userId) {

		// Add the category-user to the database
		userService.addCategoryToUser(userId, categoryId);

		//Return the categories associated with the user's id
		return JSON.stringify(categoryService.getCategoriesByUserId(userId));

	}

	// ****************************************************************************
	// This needs to be renamed. It does more than get categories
	// ****************************************************************************
	@RequestMapping(value = "/getSelectedJob", method = RequestMethod.GET)
	@ResponseBody
	public String getSelectedJob(HttpServletRequest request, ModelAndView model, @RequestParam int jobId,
			@ModelAttribute App app, @ModelAttribute JobSearchUser user) {

		// Currently not using this.
		// Does it make more sense having selectedJob in app?
		user.setSelectedJob(jobService.getJob(jobId));

		// Set the selected job within the application
		app.setSelectedJob(jobService.getJob(jobId));

		// app.setCategoriesBySelectedJob(service.getCategoriesByJobId(jobId));
		app.getSelectedJob().setCategories(categoryService.getCategoriesByJobId(jobId));

		app.getSelectedJob().setApplicants(userService.getApplicants(jobId));

		app.getSelectedJob().setEmployees(userService.getEmployees(jobId));

		app.getSelectedJob().setCategories((ArrayList<Category>) categoryService.getCategoriesByJobId(jobId));
		app.getSelectedJob().setEmployees(userService.getEmployees(jobId));

		return JSON.stringify(app);
	}

//	@RequestMapping(value = "getSelectedCategory", method = RequestMethod.GET)
//	@ResponseBody
//	public String getSelectedCategory(HttpServletRequest request, ModelAndView model, @RequestParam int categoryId,
//			@ModelAttribute("user") JobSearchUser user, @ModelAttribute App app) {
//
//		// Set the selected category
//		app.setSelectedCategory(categoryService.getCategory(categoryId));
//
//		// Set the users associated with the category.
//		// This will return only employees if the user is an employer and vice
//		// versa.
//		app.getSelectedCategory().setUsers(userService.getUsers(categoryId, user.getProfileId()));
//
//		// Set the active jobs associated with the selected category
//		app.getSelectedCategory().setJobs(jobService.getJobsByCategory(categoryId, true));
//
//		return JSON.stringify(app);
//	}

	@RequestMapping(value = "getSelectedUser", method = RequestMethod.GET)
	@ResponseBody
	public String getSelectedUser(HttpServletRequest request, ModelAndView model, @RequestParam int userId,
			@ModelAttribute JobSearchUser user) {

		// Set the selected user
		user = userService.getUser(userId);

		// Set the active jobs associated with the selected user
		user.setJobs(jobService.getJobs(userId, true));

		return JSON.stringify(user);
	}
	
	//***********************************************************************
	@RequestMapping(value="/getAppRateCriteria", method = RequestMethod.GET)
	@ResponseBody
	public String getAppRateCriteria(){
		return JSON.stringify(userService.getAppRateCriteria());
	}
	
	
	@RequestMapping(value = "rateEmployee", method = RequestMethod.GET)
	@ResponseBody
	public String rateEmployee(ModelAndView model, 
										@RequestParam int rateCriterionId,
										@RequestParam int value,			
										@RequestParam int employeeId,
										@RequestParam int jobId){

		
		Item item = new Item();
		userService.rateEmployee(rateCriterionId, employeeId, jobId, value);
		
		return JSON.stringify(item);
		
	}
	
	//***********************************************************************
	
	@RequestMapping(value = "applyForJob", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView applyForJob(HttpServletRequest request, ModelAndView model, @RequestParam int jobId,
			@ModelAttribute("user") JobSearchUser user) {

		// Add application to database
		jobService.applyForJob(jobId, user.getUserId());

		// Update user object to reflect the changes
		user.setAppliedToJobs(jobService.getAppliedToJobs(user, true));

		model.setViewName("profile");
		return model;

	}

	@RequestMapping(value = "hireApplicant", method = RequestMethod.GET)
	@ResponseBody
	public String hireApplicant(@RequestParam int userId, @RequestParam int jobId) {

		// Add employment to the database
		userService.hireApplicant(userId, jobId);

		return JSON.stringify(userService.getEmployees(jobId));
	}

}
