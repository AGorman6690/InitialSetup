package com.jobsearch.responses

import org.springframework.beans.factory.annotation.Autowired;

import com.jobsearch.model.JobSearchUser;

public class FindEmployeesResponse {
	
	
	List<FindEmployeeUser> users	
	Integer countDatesSearched
	Double radiusSearched
	String addressSearched
	Integer jobId
	
	public static class FindEmployeeUser{
		JobSearchUser user
		Integer countJobsCompleted
		Integer countDaysAvailable
		Double overallRating
	}
	
	
}
