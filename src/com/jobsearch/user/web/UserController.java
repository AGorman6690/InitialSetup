package com.jobsearch.user.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
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
@SessionAttributes({ "user" })
public class UserController {

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	CategoryServiceImpl categoryService;

	@RequestMapping(value = "/validateEmail", method = RequestMethod.GET)
	public ModelAndView validate(@RequestParam int userId, ModelAndView model,
			@ModelAttribute("user") JobSearchUser user) {

		user = userService.validateUser(userId);

		model.addObject("user", user);

		if (user.getProfile().getName().equals("Employee")) {
			model.setViewName("EmployeeProfile");
		} else if (user.getProfile().getName().equals("Employer")) {
			model.setViewName("EmployerProfile");
		}

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

		SubmitJobPostingRequestDTO job = new SubmitJobPostingRequestDTO();
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
	public ModelAndView getProfile(ModelAndView model, HttpServletRequest request,
			@ModelAttribute("user") JobSearchUser user) {

		try {

			if (user.getUserId() == 0) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				user = userService.getUserByEmail(auth.getName());

			}
			user = userService.getProfile(user);
			model.addObject("user", user);

			if (user.getCreateNewPassword() == 0) {
				if (user.getProfile().getName().equals("Employee")) {
					model.setViewName("EmployeeProfile");
				} else if (user.getProfile().getName().equals("Employer")) {
					model.setViewName("EmployerProfile");
				}
			} else {
				model.setViewName("NewPassword");
				model.addObject("newPassword", new JobSearchUser());
			}

			return model;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;

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
	public String filterEmployees(@RequestParam String city, @RequestParam String state, @RequestParam String zipCode,
			@RequestParam int radius, @RequestParam(value = "date") List<String> dates,
			@RequestParam(value = "categoryId") List<Integer> categoryIds) {

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
			@RequestParam String redirectUrl) {
		userService.resetPassword(user);

		model.setViewName("Welcome");
		model.addObject("ResetPasswordMessage", "A new password has been sent to your email");

		return model;
	}

	@RequestMapping(value = "/upload/resume", method = RequestMethod.POST)
	public void uploadResume(@RequestParam(value = "file") MultipartFile file) throws IOException {

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

		if (number == 0) {
			userService.createUsers_DummyData();

		}

		if (number == 0) {
			userService.createJobs_DummyData();
		}

	}

}
