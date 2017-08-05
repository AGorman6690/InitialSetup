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

		// Set the session complete
		// status.setComplete();

		// Return to welcome page
//		model.setViewName("Welcome");
//		model.addAttribute("user", new JobSearchUser());
//
//		List<Profile> profiles = userService.getProfiles();
//		model.addAttribute("profiles", profiles);
		
		session.invalidate();
//		session.removeAttribute("user");
		
//		session.geta
//		for(String attributeName : session.getAttributeNames()){
//			
//		}
	}

	public void setModel_Login_SignUp(boolean error, Boolean login, Model model) {

		int requestedLogin = -1;
		
		if(error){
			String errorMessage = "Email and password do not match";
			model.addAttribute("errorMessage", errorMessage);
			requestedLogin = 1;
		}
		
		
		if(login != null){
			
			if(login == true) requestedLogin = 1;
			else requestedLogin = 0;			
		}
		

		model.addAttribute("requestedLogin", requestedLogin);
		model.addAttribute("profiles", userService.getProfiles());

//		model.addAttribute("url", hostUrl);

//		if (error) {
//			model.addAttribute("errorMessage", "Username and/or Password is incorrect");
//		}
		
	}


}
