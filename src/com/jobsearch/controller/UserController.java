package com.jobsearch.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.request.CreateUserRequest;
import com.jobsearch.request.FindEmployeesRequest;
import com.jobsearch.request.LoginRequest;
import com.jobsearch.responses.LoginResponse;
import com.jobsearch.responses.UserApplicationStatusResponse;
import com.jobsearch.responses.user.CreateUserResponse;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.RatingServiceImpl;
import com.jobsearch.service.UserServiceImpl;

@Controller
// @SessionAttributes({ "user" })
public class UserController {

	@Autowired
	UserServiceImpl userService;
	@Autowired
	JobServiceImpl jobService;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	RatingServiceImpl ratingService;
	
	@ResponseBody
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String createUser(@RequestBody CreateUserRequest request) {
		
		CreateUserResponse response = userService.createUser(request);
		return JSON.stringify(response);
	}
	

	@RequestMapping(value = "/email/validate", method = RequestMethod.GET)
	public String validateEmail(@RequestParam(name = "userId") int userId, HttpSession session){
		
		userService.setSession_EmailValidation(userId, session);
		return "redirect:/user";
	}
	
	@RequestMapping(value = "/user/login_NEW_BUT_NOT_WORKING", method = RequestMethod.POST)
	@ResponseBody
	public String login_NEW_BUT_NOT_WORKING(
			HttpSession session,
			@RequestBody @ModelAttribute("user") JobSearchUser user) {
		
		LoginRequest request = null;
		LoginResponse response = userService.login(request, session);		
		return JSON.stringify(response);
	}	
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public String login(HttpSession session, @ModelAttribute("user") JobSearchUser user) {

		userService.setSession_Login(user, session);
		return "redirect:/user";

	}
		
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String viewHomepageRequest(Model model, HttpSession session) {				
		
		userService.setViewHomepageResponse(model, session);		
		return userService.getProfileJspName(session);
	}

	@RequestMapping(value = "/user/calendar", method = RequestMethod.GET)
	public String viewCalendar(Model model, HttpSession session) {

		userService.setGetProfileCalendarResponse(model, session);
		return "/event_calendar/Event_Calendar";
	}
	
	@RequestMapping(value = "/user/{userId}/job/{jobId}/application-status", method = RequestMethod.GET)
	@ResponseBody
	public String getUserApplicationStatusResponse(
			@PathVariable(value = "userId") int userId,
			@PathVariable(value = "jobId") int jobId,
			HttpSession session) {

		UserApplicationStatusResponse response = userService.getUserApplicationStatusResponse(userId, jobId, session);
		return JSON.stringify(response);
	}



	@RequestMapping(value = "/employees/find", method = RequestMethod.GET)
	public String viewFindEmployees(Model model, HttpSession session) {
		return "/find_employees/FindEmployees";
	}	
	
	@RequestMapping(value = "/employee/leave-job/{jobId}/confirm", method = RequestMethod.GET)
	public String viewLeaveJob_confirm(@PathVariable(value = "jobId") Integer jobId, Model model, HttpSession session) {

		userService.setModel_employeeLeaveJob_confirm(jobId, model, session);
		return "/terminate_employment/Employee_Leaves_Job";
	}

	@RequestMapping(value = "/find/employees/results", method = RequestMethod.POST)
	public String findEmployees(@RequestBody FindEmployeesRequest request, Model model) {

		userService.setFindEmployeesResponse(model, request, null);
		return"/find_employees/Results_Find_Employees";
	}

	@RequestMapping(value = "/newPassword", method = RequestMethod.POST)
	public ModelAndView newPassword(ModelAndView model, @ModelAttribute("user") JobSearchUser user,
			@ModelAttribute("newPassword") JobSearchUser newPassword) {

//		userService.updatePassword(newPassword.getPassword(), user.getEmailAddress());
//
//		if (user.getProfile().getName().equals("Employee")) {
//			model.setViewName("Profile_Employee");
//		} else if (user.getProfile().getName().equals("Employer")) {
//			model.setViewName("EmployerProfile");
//		}
//
		return model;

	}
	
	@ResponseBody
	@RequestMapping(value = "/user/{userId}/verify-availability/job/{jobId}", method = RequestMethod.GET)
	public String verifyAvailabilityForJob(Model model, HttpSession session, 
			@PathVariable(value = "userId") int userId,
			@PathVariable(value = "jobId") int jobId) {
		
		return userService.getAvailabliltyStatusMessage_forUserAndJob(userId, jobId);
	}
	
	@RequestMapping(value = "/user/update-home-location", method = RequestMethod.POST)
	@ResponseBody
	public String updateHomeLocation(HttpSession session,
				@RequestParam( name="city") String city,
				@RequestParam( name="state") String state,
				@RequestParam( name="zip") String zip  ) {
	
		userService.updateHomeLocation(session, city, state, zip);
		return "";
	}
	
	@RequestMapping(value = "/user/update-max-work-radius", method = RequestMethod.POST)
	@ResponseBody
	public String updateMaxWorkRadius(HttpSession session,
				@RequestParam( name="maxWorkRadius") Integer maxWorkRadius ) {
	
		userService.updateMaxWorkRadius(session, maxWorkRadius);
		return "";
	}
	
	@RequestMapping(value = "/user/update-about", method = RequestMethod.POST)
	@ResponseBody
	public String updateAbout(HttpSession session,
				@RequestParam( name="about") String about ){
	
		userService.updateAbout(session, about);
		return "";
	}


	@RequestMapping(value = "/user/password/reset", method = RequestMethod.GET)
	public ModelAndView requestNewPassword(ModelAndView model) {

		model.setViewName("ResetPassword");

		return model;
	}

	@RequestMapping(value = "/user/password/reset", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView resetPassword(ModelAndView model, @ModelAttribute("user") JobSearchUser user,
			@RequestParam(name = "redirectUrl") String redirectUrl) {
		userService.resetPassword(user);

		model.setViewName("Welcome");
		model.addObject("ResetPasswordMessage", "A new password has been sent to your email");

		return model;
	}

	@RequestMapping(value = "/upload/resume", method = RequestMethod.POST)
	public void uploadResume(@RequestParam(name = "file", value = "file") MultipartFile file) throws IOException {

		if (file != null) {
			ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
			String myString = IOUtils.toString(stream, "UTF-8");
			System.out.println(myString);
		}
	}

	


}
