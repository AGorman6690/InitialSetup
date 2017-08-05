package com.jobsearch.responses

import com.jobsearch.model.Application
import com.jobsearch.model.Job

public class ConflictingApplicationsResponse {

	Application referenceApplication
	List<ConflictingApplication> conflictingApplications
//	Integer count_conflictingApplications
	List<ConflictingApplication> conflictingApplicationsToBeRemoved
	List<ConflictingApplication> conflictingApplicationsToBeSentBackToEmployer
	List<ConflictingApplication> conflictingApplicationsToBeModifiedButRemainAtEmployer
	
	public ConflictingApplicationsResponse(){
		conflictingApplications = new ArrayList<>();
	}
	
	public static class ConflictingApplication{
		Application application
		Job job		
	}
}
