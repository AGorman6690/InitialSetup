package com.jobsearch.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class WelcomeServiceImpl {
	
	@Autowired
	UserServiceImpl userService;

	public void Logout(HttpSession session) {
		session.invalidate();
	}

	public void setModel_LoginSignUp(Model model) {
		model.addAttribute("profiles", userService.getProfiles());	
	}
}
