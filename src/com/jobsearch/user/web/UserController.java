package com.jobsearch.user.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

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

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.EmployeeSearch;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.rate.SubmitRatingDTO;
import com.jobsearch.user.rate.SubmitRatingDTOs_Wrapper;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
// @SessionAttributes({ "user" })
public class UserController {

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	ApplicationServiceImpl applicationService;

	@RequestMapping(value = "/email/validate", method = RequestMethod.GET)
	public String validateEmail(@RequestParam(name = "userId") int userId, HttpSession session){
		
		userService.setSession_EmailValidation(userId, session);
		return "redirect:/user/profile";
	}
	
	@RequestMapping(value = "/user/login", method = RequestMethod.GET)
	public String login(HttpSession session, @ModelAttribute("user") JobSearchUser user) {

		userService.setSession_Login(user, session);
		return "redirect:/user/profile";

	}	


	@RequestMapping(value = "/user/profile", method = RequestMethod.GET)
	public String getProfile_SessionUser(Model model, HttpSession session) {				
		userService.setModel_Profile(model, session);		
		return userService.getProfileJspName(session);
	}

	
	@RequestMapping(value = "/user/credentials", method = RequestMethod.GET)
	public String viewCredentials(Model model, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);
		JobSearchUserDTO userDto = new JobSearchUserDTO();		
		userDto.setUser(sessionUser);

		userService.setModel_getRatings_byUser(model, sessionUser.getUserId());
		
		model.addAttribute("isViewingOnesSelf", true);
		model.addAttribute("userDto", userDto);
		return "/credentials_employee/Credentials_Employee";
	}
	
	@RequestMapping(value = "/user/calendar", method = RequestMethod.GET)
	public String viewCalendar(Model model, HttpSession session) {

		userService.setModel_viewCalendar_employee(model, session);
		return "/event_calendar/Event_Calendar";
	}

	@ResponseBody
	@RequestMapping(value = "/user/sign-up", method = RequestMethod.POST)
	public String signUp(@RequestBody JobSearchUser user) {

		
		
		// **********************************************************
		// **********************************************************
		// For security reasons, do you think we should pass an encrypted userId
		// in the email-validation url?
		// **********************************************************
		// **********************************************************		
		

//		if (user != null) {
//			model.addAttribute("user", user);
//			return "EmailValidateMessage";
//		} else {
			return JSON.stringify(userService.createUser(user));
//		}

	}

	@RequestMapping(value = "/employees/find", method = RequestMethod.GET)
	public String viewFindEmployees(Model model, HttpSession session) {

		userService.setModel_findEmployees_pageLoad(model, session);
		return "/find_employees/FindEmployees";
	}	

	@RequestMapping(value = "/find/employees/results", method = RequestMethod.POST)
	public String findEmployees(@RequestBody EmployeeSearch employeeSearch, Model model) {

		userService.setModel_findEmployees_results(model, employeeSearch);

		return"/find_employees/Results_Find_Employees";
	}

	@RequestMapping(value = "/newPassword", method = RequestMethod.POST)
	public ModelAndView newPassword(ModelAndView model, @ModelAttribute("user") JobSearchUser user,
			@ModelAttribute("newPassword") JobSearchUser newPassword) {

		userService.updatePassword(newPassword.getPassword(), user.getEmailAddress());

		if (user.getProfile().getName().equals("Employee")) {
			model.setViewName("Profile_Employee");
		} else if (user.getProfile().getName().equals("Employer")) {
			model.setViewName("EmployerProfile");
		}

		return model;

	}

	@RequestMapping(value = "/user/{userId}/ratings", method = RequestMethod.GET)
	public String getRatings_byUser(Model model, @PathVariable(value = "userId") int userId) {
		
		userService.setModel_getRatings_byUser(model, userId);
		
		return "/ratings/RatingsByUser";
	}	
	
	@RequestMapping(value = "/user/settings/edit", method = RequestMethod.POST)
	@ResponseBody
	public String editEmployeeSettings(HttpSession session, @RequestBody JobSearchUser user_edited) {
		userService.editEmployeeSettings(user_edited, session);
		return "";
	}


	@RequestMapping(value = "/user/rate/employees", method = RequestMethod.POST)
	public String rateEmployees(HttpSession session,
									@RequestBody List<SubmitRatingDTO> submitRatingDtos) {

		userService.insertRatings(submitRatingDtos, session);

		return "redirect:/user/profile";
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
