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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.job.service.SubmitJobPostingRequestDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.user.rate.RatingRequestDTO;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
//@SessionAttributes({ "user" })
public class UserController {

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	CategoryServiceImpl categoryService;

	@RequestMapping(value = "/validateEmail", method = RequestMethod.GET)
	public ModelAndView validate(@RequestParam(name = "userId") int userId, ModelAndView model,
			@ModelAttribute("user") JobSearchUser user) {

		user = userService.validateUser(userId);

//		model.addObject("user", user);
		String view = null;
		if (user.getProfile().getName().equals("Employee")) {
			model.setViewName("EmployeeProfile");
//			view = "EmployeeProfile";
		} else if (user.getProfile().getName().equals("Employer")) {
			model.setViewName("EmployerProfile");
//			view = "EmployerProfile";
		}

//		model.addAttribute("user", user);
		model.addObject("user", user);
		return model;
	}

	@RequestMapping(value = "/user/profile", method = RequestMethod.GET)
	public String getProfile(Model model, HttpServletRequest request,
			@ModelAttribute("user") JobSearchUser user, HttpSession session) {

		try {

			if (user.getUserId() == 0) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				user = userService.getUserByEmail(auth.getName());

			}
			
			//Get the user's profile
			user = userService.getProfile(user);
			model.addAttribute("user", user);
			
			//Update session user after they have logged in
			session.setAttribute("user", user);

			String viewName = null;
			if (user.getCreateNewPassword() == 0) {
				if (user.getProfile().getName().equals("Employee")) {
//					model.setViewName("EmployeeProfile");
					viewName = "EmployeeProfile";
				} else if (user.getProfile().getName().equals("Employer")) {
//					model.setViewName("EmployerProfile");
					viewName = "EmployerProfile";
				}
			} else {
//				model.setViewName("NewPassword");
				viewName = "NewPassword";
				model.addAttribute("newPassword", new JobSearchUser());
			}

			return viewName;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;

	}	
	
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ModelAndView registerUser(ModelAndView model, @ModelAttribute("user") JobSearchUser user) {

		user = userService.createUser(user);
		model.addObject("user", user);
		model.setViewName("EmailValidateMessage");
		return model;
	}

	@RequestMapping(value = "/employees/find", method = RequestMethod.GET)
	public ModelAndView viewFindEmployees(ModelAndView model, HttpSession session) {

		JobSearchUser user = (JobSearchUser) session.getAttribute("user");		
		model.addObject("user", user);
		
		model.setViewName("FindEmployees");

//		model.setViewName("Test");
		return model;
	}
	

	@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
	public ModelAndView viewProfile(ModelAndView model) {
		model.setViewName("UserProfile");
		return model;
	}

	@RequestMapping(value = "/viewPostJob", method = RequestMethod.GET)
	public ModelAndView viewPostJob(ModelAndView model, HttpSession session) {

		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		
		model.addObject("user", user);
		
		SubmitJobPostingRequestDTO job = new SubmitJobPostingRequestDTO();
		model.addObject("job", job);

		model.setViewName("PostJob");
		return model;
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

	@RequestMapping(value = "/user/availability/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateAvailability(ModelAndView model, @RequestBody AvailabilityRequestDTO availabityRequest) {

		userService.updateAvailability(availabityRequest);
	}

	@RequestMapping(value = "/user/profile/edit", method = RequestMethod.POST)
	@ResponseBody
	public void editProfile(ModelAndView model, @RequestBody EditProfileRequestDTO editProfileRequest) {

		userService.editProfile(editProfileRequest);
	}

	@RequestMapping(value = "/employees/filter", method = RequestMethod.GET)
	@ResponseBody
	public String filterEmployees(@RequestParam(name = "city") String city, @RequestParam(name = "state") String state, 
			@RequestParam(name = "zipCode")  String zipCode, @RequestParam(name="radius") int radius, 
			@RequestParam(name = "date", value = "date") List<String> dates,
			@RequestParam(name = "categoryId", value = "categoryId") List<Integer> categoryIds) {

		FindEmployeesRequestDTO findEmployeesRequest = new FindEmployeesRequestDTO(city, state, zipCode, radius, dates,
				categoryIds);

		List<JobSearchUser> employees = userService.findEmployees(findEmployeesRequest);

		return JSON.stringify(employees);
	}

	@RequestMapping(value = "/user/rate", method = RequestMethod.POST)
	@ResponseBody
	public void rateEmployee(ModelAndView model, @RequestBody RatingRequestDTO ratingDTO) {

		userService.rateEmployee(ratingDTO);
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
	public void uploadResume(@RequestParam(name="file", value = "file") MultipartFile file) throws IOException {

		if (file != null) {
			ByteArrayInputStream stream = new   ByteArrayInputStream(file.getBytes());
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
