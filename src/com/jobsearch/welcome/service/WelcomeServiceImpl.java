package com.jobsearch.welcome.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.user.service.UserServiceImpl;

@Service
public class WelcomeServiceImpl {
	
	@Autowired
	UserServiceImpl userService;

	public void setModel_Welcome(Model model, HttpSession session) {


		session.setAttribute("profiles", userService.getProfiles());

//		model.addAttribute("url", hostUrl);

//		if (error) {
//			model.addAttribute("errorMessage", "Username and/or Password is incorrect");
//		}

	}

	public void Logout(HttpSession session) {

		// Set the session complete
		// status.setComplete();

		// Return to welcome page
//		model.setViewName("Welcome");
//		model.addAttribute("user", new JobSearchUser());
//
//		List<Profile> profiles = userService.getProfiles();
//		model.addAttribute("profiles", profiles);
		
//		session.invalidate();
		session.removeAttribute("user");
		
//		session.geta
//		for(String attributeName : session.getAttributeNames()){
//			
//		}
	}


}
