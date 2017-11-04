package com.jobsearch.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.JobUtil;

public class BaseService {
	
	@Autowired
	JobUtil jobUtil;
	
	private static JobSearchUser sessionUser;
	
	public JobSearchUser getSessionUser(HttpSession session){
		return SessionContext.getUser(session);	
	}
	
	public boolean isLoggedIn(HttpSession session) {				
		if(getSessionUser(session) != null) return true;
		else return false;
	}
	
	public boolean isEmployee(HttpSession session) {		
		sessionUser = getSessionUser(session);
		if (sessionUser != null){
			if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE){
				return true;
			}else{
				return false;	
			}		
		}		
		return false;			
	}

}
