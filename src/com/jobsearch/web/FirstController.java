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

import com.jobsearch.json.JSON;
import com.jobsearch.model.App;
import com.jobsearch.model.Category;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.DataBaseItem;
import com.jobsearch.service.FirstService;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
		//These application properties are used to set the user's property OBJECTS from the user's property IDs. 
		app.setCategories(service.getCats());
		app.setProfiles(service.getProfiles());

		model.setViewName("welcome");

		return model;
	}


	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView signIn(ModelAndView model, @ModelAttribute("user") JobSearchUser user){
		
		model.setViewName("signin");
		return model;
	}
	

	
	@RequestMapping(value="profile", method = RequestMethod.POST)
	public ModelAndView profile(ModelAndView model, @ModelAttribute("user") JobSearchUser user,
													@ModelAttribute App app){
		
		//From the email provided, set the user object
		user = service.getUserByEmail(user.getEmailAddress());
		
		//Set the user's list of category objects.
		//From the user's ID, get their associated category IDs.
		//Note: The user ID to category ID is stored in table "usercategories"
		user.setCategories(service.getCategories(user, app));
		
		//From the user's profile ID, use the application data to set the user's profile OBJECT
		user.setProfile(app.getProfileById(user.getProfileId()));

		//It appears that after setting the "user" object by email as done above,
		//the "user" object needs to be added to the model again.
		//Without this .addObject statement below, the view does not have access to the
		//"user" object after it was modified above (or so it seems...)
		model.addObject("user", user);
		
		model.setViewName("profile");
		return model;
	}
	
	
	@RequestMapping(value = "/insertUser", method = RequestMethod.GET)
	public ModelAndView insertUser(ModelAndView model, @ModelAttribute("user") JobSearchUser user, @ModelAttribute("app") App app){			
		
		model.setViewName("Registration");
		return model;
	}
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerUser(ModelAndView model, @ModelAttribute("user") JobSearchUser user, @ModelAttribute("app") App app){		
		
		service.createUser(user);
			
		model.setViewName("setCategories");		
		return model;
	}
	 
	@RequestMapping(value = "/catsDone", method = RequestMethod.POST)
	@ResponseBody
	@ModelAttribute("user")	
	public ModelAndView catsDone(ModelAndView model, @RequestParam (required = true) String[] cats,
									@ModelAttribute("user") JobSearchUser user,
									@ModelAttribute App app){
		
		//Set the user's category list from their selection
		service.setUsersCats(user, cats, app);
		
		//Because the ID column in the user table is auto incremented, the user's 
		//ID needs to be retrieved after the user was created in the table 
		service.setUsersId(user);
		
		//Export the user's categories
		service.exportUsersCats(user);
		
		model.addObject("user", user);
	
		//****************************************************
		//After the user is finished setting up their profile, the profile 
		//page should be loaded. However setting the view name to "profile" is not doing the trick.
		//Maybe you can figure it out...
		model.setViewName("profile");
		//****************************************************		
		
		
		return model;
	}
	
	@RequestMapping(value = "deleteCat", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView deleteCat(ModelAndView model, @RequestBody final DataBaseItem item, @ModelAttribute App app){
		
		//From the category name, set the category ID from the app category list
		item.setCategoryId(app.getCategoryByName(item.getCategoryName()).getId());
		
		service.deleteCat(item);
		
		return model;
		
	}
	
	@RequestMapping(value = "addCat", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView addCat(ModelAndView model, @RequestBody final DataBaseItem item, @ModelAttribute App app){
		
		//From the item's category name, set the item'scategory ID from the app's category list
		item.setCategoryId(app.getCategoryByName(item.getCategoryName()).getId());
		
		service.addCat(item);
		
		model.setViewName("profile");
		return model;
		
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
