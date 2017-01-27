package com.jobsearch.session;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.jobsearch.job.service.FindJobFilterDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

public class SessionContext {
	
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

	public static void appendToFilteredJobIds(HttpSession session, List<Integer> jobIdsToAdd) {
		
		if(jobIdsToAdd != null){			
			
			@SuppressWarnings("unchecked")
			List<Integer> jobIds_alreadyFiltered = (List<Integer>) session.getAttribute(SESSION_ATTRIBUTE_FILTERED_JOB_IDS);
			
			// If jobs have already been filtered (i.e. the reques t is to "Get More Jobs")
			if(jobIds_alreadyFiltered != null){
				jobIdsToAdd.addAll(jobIds_alreadyFiltered);					
			}
				
			setFilteredJobIds(session, jobIdsToAdd);		
					
		}
		
		
	}

	public static void setFilteredJobIds(HttpSession session, List<Integer> value) {
		session.setAttribute(SESSION_ATTRIBUTE_FILTERED_JOB_IDS, value);
		
	}

	@SuppressWarnings("unchecked")
	public static List<Integer> getFilteredJobIds(HttpSession session) {
		return (List<Integer>) session.getAttribute(SessionContext.SESSION_ATTRIBUTE_FILTERED_JOB_IDS);
	}

	public static String get404Page() {
		
		return "/error/404";
	}

	public static FindJobFilterDTO getLastFilterRequest(HttpSession session) {
		
		return (FindJobFilterDTO) session.getAttribute("lastFilterRequest");
	}



}
