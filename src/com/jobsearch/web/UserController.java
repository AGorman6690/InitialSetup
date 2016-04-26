package com.jobsearch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.jobsearch.job.service.CompletedJobDTO;
import com.jobsearch.job.service.CreateJobDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.DummyData;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.Profile;
import com.jobsearch.user.rate.RatingDTO;
import com.jobsearch.user.service.AvailabilityDTO;
import com.jobsearch.user.service.EditProfileDTO;
import com.jobsearch.user.service.FindEmployeesDTO;
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

	@RequestMapping(value = "/employees/find", method = RequestMethod.GET)
	public ModelAndView viewFindEmployees(ModelAndView model) {
		model.setViewName("FindEmployees");
		return model;
	}
	
	@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
	public ModelAndView viewProfile(ModelAndView model) {
		model.setViewName("UserProfile");
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

	@RequestMapping(value = "/user/profile", method = RequestMethod.GET)
	public ModelAndView getProfile(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {
		
		try {

			if (user.getUserId() == 0) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
				user = userService.getUserByEmail(auth.getName());
			}
			
			user = userService.getProfile(user);			

			model.addObject("user", user);

			if (user.getProfileId() == 1)
				model.setViewName("EmployerProfile");
			else if(user.getProfileId() == 2)
				model.setViewName("EmployeeProfile");

			return model;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return null;

	}

	
	@RequestMapping(value = "/user/availability/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateAvailability(ModelAndView model, @RequestBody AvailabilityDTO availabilityDTO){

		userService.updateAvailability(availabilityDTO);
	}
	

	@RequestMapping(value = "/user/profile/edit", method = RequestMethod.POST)
	@ResponseBody
	public void editProfile(ModelAndView model, @RequestBody EditProfileDTO editProfileDTO){

		userService.editProfile(editProfileDTO);
	}

	
	@RequestMapping(value = "/employees/filter", method = RequestMethod.GET)
	@ResponseBody
	public String filterEmployees(@RequestParam String city, @RequestParam String state,
					@RequestParam String zipCode, @RequestParam int radius,
					@RequestParam(value="date") List<String> dates,
					@RequestParam(value="categoryId") List<Integer> categoryIds) {
		
		
		FindEmployeesDTO findEmployeesDto = new FindEmployeesDTO(city, state, zipCode, radius,
													dates, categoryIds);
		
		List<JobSearchUser> employees = userService.findEmployees(findEmployeesDto);
		
		return JSON.stringify(employees);
	}	

	@RequestMapping(value = "/user/rate", method = RequestMethod.POST)
	@ResponseBody
	public void rateEmployee(ModelAndView model, @RequestBody RatingDTO ratingDTO){

		userService.rateEmployee(ratingDTO);
	}
	
	@RequestMapping(value = "/dummyData", method = RequestMethod.GET)
	@ResponseBody
	public void setDummyData(){
		
		//Change this and the following conditions
//		if you wish to create dummy data
		int number = 0;
		
		if(number == 0){
			userService.createUsers_DummyData();
	
		}
		
		if(number == 0){
			userService.createJobs_DummyData();
		}

	}

}
