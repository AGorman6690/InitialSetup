package com.jobsearch.responses

import com.jobsearch.model.Job
import com.jobsearch.model.WorkDay
import com.jobsearch.utilities.StringUtility;

public class FindJobsResponse {
	Double maxDistance
	List<JobDto_findJobsResponse> jobDtos	
	float latitudeSearched
	float longitudeSearched
	boolean appendedJobs
	public static class JobDto_findJobsResponse{		
		Job job
		List<WorkDay> workDays
		Double employerOverallRating	
		String employerName
		Double distance	
	}
}
