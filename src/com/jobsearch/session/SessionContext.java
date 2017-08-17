package com.jobsearch.session;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.jobsearch.model.JobSearchUser;
import com.jobsearch.service.UserServiceImpl;

public class SessionContext {
	
	@Autowired
	static UserServiceImpl userService;
	
	public static String SESSION_ATTRIBUTE_FILTERED_JOB_IDS = "loadedFilteredJobIds";
	
	public static JobSearchUser getUser(HttpSession session) {
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
		//
		// Once the session attributes mature, revisit this.
		// *************************************
		// *************************************
		JobSearchUser user = SessionContext.getUser(session);
		
		if(user != null) return true;
		else return false;
//		if (user == null) {
//			return false;
//		}
//		else if(user.getEmailAddress() == null){
//			return false;
//		}
//		else {
//			if (user.getEmailAddress() == null) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
	}

	@SuppressWarnings("unchecked")
	public static List<Integer> getFilteredJobIds(HttpSession session) {
		return (List<Integer>) session.getAttribute(SessionContext.SESSION_ATTRIBUTE_FILTERED_JOB_IDS);
	}

	public static String get404Page() {
		
		return "/error/404";
	}
	
	public static void setUser(HttpSession session, JobSearchUser user) {		
		session.setAttribute("user", user);		
	}


}
