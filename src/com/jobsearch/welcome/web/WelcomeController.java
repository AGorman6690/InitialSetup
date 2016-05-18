package com.jobsearch.welcome.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.user.service.UserServiceImpl;

@Controller
@SessionAttributes({ "user" })
public class WelcomeController {

	@Autowired
	UserServiceImpl userService;

	@Value("${host.url}")
	private String hostUrl;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome(ModelAndView model, HttpServletRequest request,
			@RequestParam(required = false) boolean error) {

		// // Set session objects
		JobSearchUser user = new JobSearchUser();

		List<Profile> profiles = userService.getProfiles();
		model.addObject("profiles", profiles);
		model.addObject("user", user);

		model.addObject("url", hostUrl);

		if (error) {
			model.addObject("errorMessage", "Username and/or Password is incorrect");
		}

		model.setViewName("Welcome");

		return model;
	}
}
