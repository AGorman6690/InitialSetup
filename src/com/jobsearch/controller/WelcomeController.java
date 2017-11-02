package com.jobsearch.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobsearch.model.JobSearchUser;
import com.jobsearch.repository.UserRepository;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.service.WelcomeServiceImpl;

@Controller
public class WelcomeController {

	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	WelcomeServiceImpl welcomeService;	
	
	@Autowired
	UserRepository temp;

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
		
		
//		List<String> expressions = temp.getExpressions();
//		model.addAttribute("expressions", expressions);	
//		return "calculator";
	}

	@RequestMapping(value = "/equation/save", method = RequestMethod.POST)
	public String save(Model model, HttpSession session,
					@RequestBody String equation) {
			
		temp.saveEquation(equation);
		List<String> expressions = temp.getExpressions();
		model.addAttribute("expressions", expressions);
//		return "Welcome";
		return "calculator_history";
	}
	
	@RequestMapping(value = "/equation/history", method = RequestMethod.GET)
	public String getEquationHistory(Model model) {
			
		List<String> expressions = temp.getExpressions();
		model.addAttribute("expressions", expressions);
//		return "Welcome";
		return "calculator_history";
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
