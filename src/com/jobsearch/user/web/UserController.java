package com.jobsearch.user.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.FindEmployeesDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.session.SessionContext;
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
	public String getProfile(Model model, HttpSession session) {				
		userService.setModel_Profile(model, session);		
		return userService.getProfileJspName(session);
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
	public ModelAndView viewFindEmployees(ModelAndView model, HttpSession session) {

		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		model.addObject("user", user);

		model.setViewName("FindEmployees");

		// model.setViewName("Test");
		return model;
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ModelAndView viewSettings(ModelAndView model, HttpSession session) {

		JobSearchUser user = (JobSearchUser) session.getAttribute("user");

		if (user.getProfileId() == 1) {
			model.setViewName("EmployeeSettings");
		}

		return model;
	
	}
	
	@RequestMapping(value = "/availability/update", method = RequestMethod.POST)
	public String updateAvailability(@RequestBody AvailabilityDTO availabilityDto,
										Model model, HttpSession session) {

		userService.updateAvailability(session, availabilityDto);
		userService.setModel_Availability(model, session);
		
		return "settings_employee/AvailableDays_CurrentlySet";
	}
	
	@RequestMapping(value = "/availability", method = RequestMethod.GET)
	public String viewAvailability(Model model, HttpSession session) {

		userService.setModel_Availability(model, session);

		return "settings_employee/Availability";
	}

	
	@RequestMapping(value = "/user/credentials", method = RequestMethod.GET)
	public String viewCredentials(Model model, HttpSession session) {

		userService.setModel_Credentials_Employee(model, session);

		return "credentials_employee/Credentials_Employee";
	}

		
	@RequestMapping(value = "/user/{userId}/jobs/completed", method = RequestMethod.GET)
	public String getUserWorkHistory(@PathVariable(value = "userId") int userId,
									 Model model, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			userService.setModel_WorkHistoryByUser(model, userId);
			return "templates/WorkHistory";
		} else {
			return "NotLoggedIn";
		}

	}

	@RequestMapping(value = "/newPassword", method = RequestMethod.POST)
	public ModelAndView newPassword(ModelAndView model, @ModelAttribute("user") JobSearchUser user,
			@ModelAttribute("newPassword") JobSearchUser newPassword) {

		userService.updatePassword(newPassword.getPassword(), user.getEmailAddress());

		if (user.getProfile().getName().equals("Employee")) {
			model.setViewName("EmployeeProfile");
		} else if (user.getProfile().getName().equals("Employer")) {
			model.setViewName("EmployerProfile");
		}

		return model;

	}

	@RequestMapping(value = "/user/settings/edit", method = RequestMethod.POST)
	@ResponseBody
	public void editEmployeeSettings(HttpSession session, @RequestBody EditProfileRequestDTO editProfileRequest) {

		userService.editEmployeeSettings(editProfileRequest, session);
	}

	@RequestMapping(value = "/search/employees", method = RequestMethod.GET)
	@ResponseBody
	public String findEmployees(@RequestParam(name = "fromAddress", required = true) String fromAddress,
			@RequestParam(name = "radius", required = true) double radius,
			@RequestParam(name = "day", value = "day", required = false) List<String> days,
			@RequestParam(name = "rating", required = false) double rating,
			@RequestParam(name = "categoryId", value = "categoryId", required = false) List<Integer> categoryIds) {

		// Set the dto
		FindEmployeesDTO findEmployeesDto = new FindEmployeesDTO(fromAddress, radius, rating, days, categoryIds);

		// Run the velocity template
		String findEmployeesResponseHTML = userService.getFindEmployeesResponseHTML(findEmployeesDto);

		return findEmployeesResponseHTML;
	}

	@RequestMapping(value = "/user/rate", method = RequestMethod.POST)
	public String rateEmployee(Model model, @RequestBody SubmitRatingDTOs_Wrapper submitRatingDtos_wrapper) {

		userService.insertRatings(submitRatingDtos_wrapper);

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

	@RequestMapping(value = "/dummyData", method = RequestMethod.GET)
	@ResponseBody
	public void setDummyData() {

		// Change this and the following conditions
		// if you wish to create dummy data
		int number = 0;

		if (number == 1) {
			userService.createUsers_DummyData();

		}

		if (number == 1) {
			userService.createJobs_DummyData();
		}

	}

}
