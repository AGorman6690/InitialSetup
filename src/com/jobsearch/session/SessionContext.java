package com.jobsearch.session;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.jobsearch.model.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

public class SessionContext {
	

	
	public static JobSearchUser getSessionUser(HttpSession session) {
		return (JobSearchUser) session.getAttribute("user");
	}

	public static void verifyLoggedInUser(HttpSession session, Model model) {
		
		if (!isLoggedIn(session)) {
			JobSearchUser user = new JobSearchUser();
			model.addAttribute("user", user);
		}
		
	}
	
	public static boolean isLoggedIn(HttpSession session) {

		// *************************************
		// *************************************
		// This is hackish.
		// Sometimes this user session attribute is not null...
		// That is why the email is verified not to be null after the
		// user is apparently "not" null...
		// *************************************
		// *************************************
		JobSearchUser user = SessionContext.getSessionUser(session);
		if (user == null) {
			return false;
		}
		else if(user.getEmailAddress() == null){
			return false;
		}
		else {
			if (user.getEmailAddress() == null) {
				return false;
			} else {
				return true;
			}

		}
	}
}
