package com.jobsearch.welcome.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobsearch.model.JobSearchUser;
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

		return "Welcome";
	}
	
	
	@RequestMapping(value = "/login-signup", method = RequestMethod.GET)
	public String login_signUp(Model model,
					@RequestParam(name = "error", required = false) boolean error,
					@RequestParam(name = "login", required = false) Boolean login) {
		
		welcomeService.setModel_Login_SignUp(error, login, model);
		
		
		return "Login_SignUp";
	}
	
	
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public String logout(Model model, HttpSession session) {
		
		welcomeService.Logout(session);

		return "redirect:/";
	}
}
