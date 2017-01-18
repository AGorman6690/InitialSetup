package com.jobsearch.welcome.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.welcome.service.WelcomeServiceImpl;

@Controller
public class WelcomeController {

	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	WelcomeServiceImpl welcomeService;	

	@Value("${host.url}")
	private String hostUrl;

	@ModelAttribute("user")
	public JobSearchUser getSessionUser() {
		JobSearchUser sessionUser = new JobSearchUser();
		return sessionUser;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(Model model, HttpSession session,
					@RequestParam(name = "error", required = false) boolean error) {

		welcomeService.setModel_Welcome(model, session);

		return "Welcome";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model, HttpSession session) {
		
		welcomeService.Logout(session);
	
		return "redirect:/";
	}
}
