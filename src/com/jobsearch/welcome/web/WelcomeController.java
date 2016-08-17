package com.jobsearch.welcome.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
@SessionAttributes({ "user", "loadedFilteredJobIds", "loadedFilteredJobs"})
public class WelcomeController {

	@Autowired
	UserServiceImpl userService;

	@Value("${host.url}")
	private String hostUrl;
	
	@ModelAttribute("user")
	public JobSearchUser getSessionUser(){
		JobSearchUser sessionUser = new JobSearchUser();
		return sessionUser;
	}
	
	@ModelAttribute("loadedFilteredJobIds")
	public List<Integer> getSessionLoadedFilteredJobIds(){
		List<Integer> loadedFilteredJobIds = new ArrayList<Integer>();
		return loadedFilteredJobIds;
	}
	
	@ModelAttribute("loadedFilteredJobs")
	public List<Job> getSessionLoadedFilteredJobs(){
		List<Job> loadedFilteredJobs = new ArrayList<Job>();
		return loadedFilteredJobs;
	}	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(Model model, HttpServletRequest request,
			@RequestParam(name = "error", required = false) boolean error) {


		// // Set session objects
//		JobSearchUser user = new JobSearchUser();

		List<Profile> profiles = userService.getProfiles();
		model.addAttribute("profiles", profiles);
//		model.addAttribute("user", user);

		model.addAttribute("url", hostUrl);

		if (error) {
			model.addAttribute("errorMessage", "Username and/or Password is incorrect");
		}

//		model.setViewName("Welcome");

		return "Welcome";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(ModelAndView model, SessionStatus status,
						@ModelAttribute("user") JobSearchUser user) {
		//*******************************************
		//*******************************************
		//Need to figure this out
		//*******************************************
		//*******************************************
		
		//Set the session complete
//		status.setComplete();
		

		
		//Return to welcome page
		model.setViewName("Welcome");

		user = new JobSearchUser();

		model.addObject("user", user);

		List<Profile> profiles = userService.getProfiles();
		model.addObject("profiles", profiles);

		return model;
	}
}
