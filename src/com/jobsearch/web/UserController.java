package com.jobsearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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

import com.jobsearch.json.JSON;
import com.jobsearch.model.App;
import com.jobsearch.model.Category;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.DataBaseItem;
import com.jobsearch.model.Job;
import com.jobsearch.service.FirstService;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"user", "app"})
public class FirstController {
	
//	@ModelAttribute
//	public JobSearchUser addUser(){
//		JobSearchUser user = new JobSearchUser();
//		return user;
//	}

	@Autowired
	FirstService service;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome(ModelAndView model) {
		
		//Set session objects
		JobSearchUser user = new JobSearchUser();
		App app = new App();			
		
		model.addObject("user", user);
		model.addObject("app", app);
		
		//Initialize the application specific properties.
		//These application properties are used to hold all the user's options such
		//as category options to associate with jobs.
		app.setCategories(service.getCategories());
		app.setProfiles(service.getProfiles());

		model.setViewName("welcome");

		return model;
	}


	
	@RequestMapping(value = "/signIn", method = RequestMethod.GET)
	public ModelAndView signIn(ModelAndView model, @ModelAttribute("user") JobSearchUser user){
		
		model.setViewName("SignIn");
		return model;
	}
	

	
	@RequestMapping(value="getProfile", method = RequestMethod.GET)
	public ModelAndView getGrofile(ModelAndView model, @ModelAttribute("user") JobSearchUser user,
											@ModelAttribute App app){
		
		//From the email provided, set the user object
		user = service.getUserByEmail(user.getEmailAddress());
		
		//Set the user's list of category objects.
		user.setCategories(service.getCategoriesByUserId(user.getUserId()));
		
		//Set the user's profile object
		user.setProfile(service.getProfile(user.getProfileId()));
		
		//Set all jobs, active and inactive
		user.setJobs(service.getJobs(user));
		
		//Set active jobs
		user.setActiveJobs(service.getJobs(user, true));
		
		//If an employee, set applied to jobs and employement
		if (user.getProfileId() == 2){
			user.setAppliedToJobs(service.getAppliedToJobs(user, true));
			user.setEmployment(service.getEmployment(user,false));
		}
		
		
		//For each user's job, set the categories associated with each job.
		for(Job job : user.getJobs()){
			//job.setCategories(service.getCategoriesForJob(job));
			job.setCategories(service.getCategoriesByJobId(job.getId()));
		}

		//It appears that after setting the "user" object by email as done above,
		//the "user" object needs to be added to the model again.
		//Without this .addObject statement below, the view does not have access to the
		//"user" object after it was modified above (or so it seems...)
		model.addObject("user", user);
		
		//Reset the selected job.
		//This will clear controls on profile page.
		app.setSelectedJob(new Job()); 
		
		
		//Set the view to return
		if (user.getProfileId() == 2) model.setViewName("ProfileYee");
		else model.setViewName("ProfileYer");
		
		return model;
	}
	
	
	@RequestMapping(value = "/createUser", method = RequestMethod.GET)
	public ModelAndView createUser(ModelAndView model, @ModelAttribute("user") JobSearchUser user, @ModelAttribute("app") App app){			
		
		model.setViewName("RegisterUser");
		return model;
	}
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ModelAndView registerUser(ModelAndView model, @ModelAttribute("user") JobSearchUser user, @ModelAttribute("app") App app){		
		
		service.createUser(user);
		
		//Need to set the user by email in order to set the user's id.
		//When the user is created in the database, the user table's id field is auto incremented.
		//Thus, the user's id needs to be fetched after it is created in the database.
		user = service.getUserByEmail(user.getEmailAddress());
		
		//Apparently the user object needs to be re-added to the model in order for the view
		//to see the changes to the user object...
		model.addObject("user", user);
			
		model.setViewName("RegisterCategories");		
		return model;
	}
	 
//	@RequestMapping(value = "/catsDone", method = RequestMethod.POST)
//	@ResponseBody
//	@ModelAttribute("user")	
//	public ModelAndView catsDone(ModelAndView model, @RequestParam (required = true) String[] cats,
//									@ModelAttribute("user") JobSearchUser user,
//									@ModelAttribute App app){
//		
//		//Set the user's category list from their selection
//		service.setUsersCats(user, cats, app);
//		
//		//Because the ID column in the user table is auto incremented, the user's 
//		//ID needs to be retrieved after the user was created in the table 
//		service.setUsersId(user);
//		
//		//Export the user's categories
//		service.exportUsersCats(user);
//		
//		model.addObject("user", user);
//	
//		//****************************************************
//		//After the user is finished setting up their profile, the profile 
//		//page should be loaded. However setting the view name to "profile" is not doing the trick.
//		//Maybe you can figure it out...
//		model.setViewName("profile");
//		//****************************************************		
//		
//		
//		return model;
//	}
	
	@RequestMapping(value = "deleteCategory", method = RequestMethod.GET)
	@ResponseBody
	public String deleteCategory(ModelAndView model, @RequestParam int categoryId,
										@ModelAttribute App app,
										@ModelAttribute("user") JobSearchUser user){
		
		//Update database
		service.deleteCategory(user.getUserId(), categoryId);
		
		//Update user object to reflect changes
		user.setCategories(service.getCategoriesByUserId(user.getUserId()));
		
		return JSON.stringify(user);
		
	}
	
	@RequestMapping(value = "addCategory", method = RequestMethod.GET)
	@ResponseBody
	public String addCategory(ModelAndView model, @RequestParam int categoryId, 
								@ModelAttribute("user") JobSearchUser user){
		
		//Add the category-user to the database
		service.addCategory(user.getUserId(), categoryId);
		
		//Reset the user's category objects
		user.setCategories(service.getCategoriesByUserId(user.getUserId()));
			
		return JSON.stringify(user);
		
	}
	
	@RequestMapping(value = "addCategoryToJob", method = RequestMethod.GET)
	@ResponseBody            
	public String addCategoryToJob(ModelAndView model, @RequestParam int jobId,
									@RequestParam int categoryId,
									@ModelAttribute App app){

		service.addJobCategory(jobId, categoryId);
		
		//Update selected job's categories to reflect the changes.
		//NOTE: the app's selected job object is set when user clicks job name.
		app.getSelectedJob().setCategories(service.getCategoriesByJobId(jobId));
		
		model.setViewName("profile");
		return JSON.stringify(app);

	}
	
	@RequestMapping(value = "addJob", method = RequestMethod.GET)
	@ResponseBody
	public String addJob(ModelAndView model, @RequestParam String jobName,
						 @ModelAttribute("user") JobSearchUser user){

//		DataBaseItem item = new DataBaseItem();
//		item.setJobName(jobName);
//		item.setUserId(userId);
		
		//Add the job to the database
		service.addJob(jobName, user.getUserId());
		
		//Set the user's jobs to reflect the changes
		user.setJobs(service.getJobs(user.getUserId(), true));
		
		
//		model.setViewName("profile");
//		return model;
		
		return JSON.stringify(user);
		
	}
	
//	@RequestMapping(value = "/markJobComplete", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView markJobComplete(ModelAndView model, @RequestBody final DataBaseItem item,
//											@ModelAttribute App app){
	@RequestMapping(value = "/markJobComplete", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public String markJobComplete(ModelAndView model, @RequestParam int jobId,
											@ModelAttribute App app,
											@ModelAttribute("user") JobSearchUser user){
		//Update database
		service.updateJobComplete(jobId);
		
		//Update user's active jobs to reflect the changes
		user.setActiveJobs(service.getJobs(user.getUserId(), true));
		
		return JSON.stringify(user);

	}
	
	
	@RequestMapping(value = "findEmployees", method = RequestMethod.GET)
	public ModelAndView findEmployees(ModelAndView model, @ModelAttribute JobSearchUser user){
		
		model.setViewName("FindEmployees");
		return model;
		
	}
	
	@RequestMapping(value = "findJobs", method = RequestMethod.GET)
	public ModelAndView findJobs(ModelAndView model, @ModelAttribute JobSearchUser user){
		
		model.setViewName("FindJobs");
		return model;
		
	}
	
	//****************************************************************************
	//This needs to be renamed. It does more than get categories
	//****************************************************************************
	@RequestMapping(value ="/getSelectedJob", method = RequestMethod.GET)
	@ResponseBody
	public String getSelectedJob(ModelAndView model, @RequestParam int jobId,
										@ModelAttribute App app,
										@ModelAttribute JobSearchUser user){

		
		//Currently not using this.
		//Does it make more sense having selectedJob in app?
		user.setSelectedJob(service.getJob(jobId));
		
		//Set the selected job within the application
		app.setSelectedJob(service.getJob(jobId));
	
		//app.setCategoriesBySelectedJob(service.getCategoriesByJobId(jobId));
		app.getSelectedJob().setCategories(service.getCategoriesByJobId(jobId));
		
		app.getSelectedJob().setApplicants(service.getApplicants(jobId));
		
		app.getSelectedJob().setEmployees(service.getEmployees(jobId));
		

		
		
		app.getSelectedJob().setCategories((ArrayList<Category>) service.getCategoriesByJobId(jobId));
		app.getSelectedJob().setEmployees(service.getEmployees(jobId));
		
		return JSON.stringify(app);
	}
	
//	@RequestMapping(value = "getJobs", method = RequestMethod.GET)
//	@ResponseBody
//	public String getJobs(ModelAndView model, @RequestParam int categoryId,
//											@RequestParam int profileId,
//											@ModelAttribute App app){	
//		
//		app.setUsersBySelectedCat(service.getUsers(categoryId, profileId));
//		app.setJobsBySelectedCat(service.getJobsBySelectedCat(categoryId));
//				
//		return JSON.stringify(app);
//	}
	
	
	@RequestMapping(value = "getSelectedCategory", method = RequestMethod.GET)
	@ResponseBody
	public String getSelectedCategory(ModelAndView model, @RequestParam int categoryId,
											@ModelAttribute("user") JobSearchUser user,
											@ModelAttribute App app){	
		
		//Set the selected category 
		app.setSelectedCategory(service.getCategory(categoryId));
		
		//Set the users associated with the category.
		//This will return only employees if the user is an employer and vice versa.
		app.getSelectedCategory().setUsers(service.getUsers(categoryId, user.getProfileId()));		
		
		//Set the active jobs associated with the selected category
		app.getSelectedCategory().setJobs(service.getJobsByCategory(categoryId, true));
		
		return JSON.stringify(app);
	}
	
	

	@RequestMapping(value = "getSelectedUser", method = RequestMethod.GET)
	@ResponseBody
	public String getSelectedUser(ModelAndView model, @RequestParam int userId,
											@ModelAttribute JobSearchUser user,
											@ModelAttribute App app){	
		
		//Set the selected user 
		app.setSelectedUser(service.getUser(userId));
		
		//Set the active jobs associated with the selected user
		app.getSelectedUser().setJobs(service.getJobs(userId, true));

		return JSON.stringify(app);
	}
	
	
	
//	@RequestMapping(value = "getEmployees", method = RequestMethod.GET)
//	@ResponseBody
//	public String getEmployees(ModelAndView model, @RequestParam int jobId,
//											@ModelAttribute App app){	
//		
////		app.setUsersBySelectedCat(service.getUsers(categoryId, profileId));
////		app.setJobsBySelectedCat(service.getJobsBySelectedCat(categoryId));
//		
//		app.getSelectedJob().setEmployees(service.getEmployees(jobId));
//
//		
//		return JSON.stringify(app);
//	}
	
	
//	@RequestMapping(value = "getJobsByUserId", method = RequestMethod.GET)
//	@ResponseBody
//	public String getJobsByUserId(ModelAndView model, @RequestParam (required = true) int userId,
//											@ModelAttribute App app){		
//		
//		app.setJobsBySelectedUser(service.getJobs(userId, true));
//		
//		return JSON.stringify(app);
//	}	
	
	
	@RequestMapping(value = "applyForJob", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView applyForJob(ModelAndView model, @RequestParam int jobId,
										@ModelAttribute("user") JobSearchUser user){

		//Add application to database
		service.applyForJob(jobId, user.getUserId());
		
		//Update user object to reflect the changes
		user.setAppliedToJobs(service.getAppliedToJobs(user, true));
		
		model.setViewName("profile");
		return model;
		
	}
	
	
	@RequestMapping(value = "hireApplicant", method = RequestMethod.GET)
	@ResponseBody
	public String hireApplicant(ModelAndView model, 
										@RequestParam int userId,
										@RequestParam int jobId,
										@ModelAttribute JobSearchUser user,
										@ModelAttribute App app){
		
		//Add employment to the database
		service.hireApplicant(userId, jobId);
		
		//Update the employees for the selected job to reflect the changes
		app.getSelectedJob().setEmployees(service.getEmployees(jobId));
		
		return JSON.stringify(app);
		
	}
	
	
//	@RequestMapping(value = "/setUser", method = RequestMethod.POST)//, produces = APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public String setUser(ModelAndView model,  @RequestBody final JobSearchUser user) {
//	//public ModelAndView setUser(ModelAndView model,  @RequestBody final JobSearchUser user) {
//		 service.createUser(user);
//		 
//		 //model.setViewName("setCategories");
//		 return "setCategories";
//		
//	}
	
	
//	@RequestMapping(value = "/editCategories", method = RequestMethod.GET)
//	public ModelAndView edit(ModelAndView model, @ModelAttribute("app") App app){
//		
//		app.setCategories(service.getCats());
//			
//		model.addObject("list", app);
//		return model;
//	}
	
	
//	@RequestMapping(value = "/anotherpage", method = RequestMethod.GET)
//	public ModelAndView getUser1(ModelAndView model) {
//
//		/*
//		 * This can be used to get a user from database using the id passed in on the url 
//		 * don't have the database set up yet
//		 */
//		// JobSearchUser user = service.getUser(userId);
//
//		JobSearchUser user = new JobSearchUser();
//
//		user.setFirstName("Aaron");
//		user.setLastName("Gorman");
//
//		model.addObject("user", user);
//		model.setViewName("anotherpage");
//
//		return model;
//	}
	
	
//	@RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public String getUser(ModelAndView model, @RequestParam (required = true) int userId) {
//
//		JobSearchUser user = service.getUser(userId);
//		
//		return JSON.stringify(user);
//	}	
	
	
//	@RequestMapping(value="profile", method = RequestMethod.GET)
//	public ModelAndView profileGet(ModelAndView model){
//		
//		//***************************************
//		//I thought this GET method would refresh the profile.jsp.
//		//Does not seem to be the case.
//		//***************************************		
//		
//		model.setViewName("profile");
//		return model;
//	}

}
