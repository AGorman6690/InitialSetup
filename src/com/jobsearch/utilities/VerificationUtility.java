package com.jobsearch.utilities;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.WorkDay;
import com.jobsearch.session.SessionContext;

public final class VerificationUtility {
	
	public static boolean isPositiveNumber(Integer number){
		
		if(number != null && number > 0) return true;
		else return false;
	}
	
	public static boolean isPositiveNumber(Double number){
		
		if(number != null && number > 0) return true;
		else return false;
	}

	public static boolean didSessionUserPostJob(HttpSession session, Job job) {

		if(job.getUserId() == SessionContext.getUser(session).getUserId()) return true;
		else return false;
	}

	public static <T> boolean isListPopulated(List<T> list) {
		
		if(list != null && list.size() > 0) return true;
		else return false;
	}

	public static boolean isValidLocation(Job job) {		
		
		if((job.getStreetAddress() == null || job.getStreetAddress().matches("")) && 
				(job.getCity() == null || job.getCity().matches("")) && 
				(job.getState() == null || job.getState().matches("")) && 
				(job.getZipCode() == null || job.getZipCode().matches(""))){
			return false;
		}
		else return true;
	
	}
	
}
