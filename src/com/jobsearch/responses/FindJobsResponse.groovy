package com.jobsearch.responses

import com.jobsearch.model.Job
import com.jobsearch.model.WorkDay

public class FindJobsResponse {
	Double maxDistance
	List<JobDto_findJobsResponse> jobDtos	
	public static class JobDto_findJobsResponse{		
		Job job
		List<WorkDay> workDays
		Double employerRating	
		Double distance	
	}
}
