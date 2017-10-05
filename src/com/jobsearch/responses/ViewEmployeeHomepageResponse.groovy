package com.jobsearch.responses

import com.jobsearch.model.Application
import com.jobsearch.dtos.CompletedJobDto
import com.jobsearch.dtos.ProfileInfoDto;
import com.jobsearch.model.Job
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.Proposal

class ViewEmployeeHomepageResponse {
	
	List<ApplicationProgressResponse> applicationProgressResponses

	List<ApplicationProgressStatus> ApplicationProgressStatuses
	long countProposals_waitingOnYou;
	long countProposals_waitingOnYou_new;
	long countProposals_waitingOnOther;
	long countJobs_employment;
	ProfileInfoDto profileInfoDto;
	
	public ViewEmployeeHomepageResponse(){
		ApplicationProgressStatuses = new ArrayList<>();	
		applicationProgressResponses = new ArrayList<>();
	}

	public static class ApplicationProgressStatus{
		
		// ******************************************
		// Refactor this.
		// The sub class in ApplicationProgressResponse is identical
		// ******************************************
		
		Application application
		Job job	
		
		int currentProposalId
		Proposal currentProposal
		Proposal previousProposal
		
		String currentProposalLabel
		String previousProposalLabel
		
		String currentProposalStatus
		Boolean isCurrentProposalExpired
		Boolean isProposedToSessionUser
		String time_untilEmployerApprovalExpires
		
		List<String> messages
		
		int countJobWorkDays
		
		
		// properties set for the employee
		
	}
}
