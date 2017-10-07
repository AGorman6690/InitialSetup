package com.jobsearch.responses

import com.jobsearch.model.Application
import com.jobsearch.model.Job

public class ConflictingApplicationsResponse {

	Application referenceApplication
	List<ConflictingApplication> conflictingApplications
//	Integer count_conflictingApplications
	List<ConflictingApplication> conflictingApplicationsToBeRemoved_jobDoesNotAllowPartial
	List<ConflictingApplication> conflictingApplicationsToBeRemoved_applicantHasNoAvailability
	List<ConflictingApplication> conflictingApplicationsToBeSentBackToEmployer
	List<ConflictingApplication> conflictingApplicationsToBeModifiedButRemainAtEmployer
	boolean jobHasOnlyOneWorkDay
								
	
	public ConflictingApplicationsResponse(){
		conflictingApplications = new ArrayList<>();
	}
	
	public static class ConflictingApplication{
		Application application
		Job job		
	}
}
