package com.jobsearch.responses

import java.util.List;

import com.jobsearch.application.service.Application;
import com.jobsearch.job.service.Job
import com.jobsearch.model.Proposal;
import com.jobsearch.model.Question;

class ViewEmployeeHomepageResponse {
	
	List<ApplicationProgressStatus> ApplicationProgressStatuses
	long countProposals_waitingOnYou;
	long countProposals_waitingOnYou_new;
	long countProposals_waitingOnOther;
	long countJobs_employment;
	
	public ViewEmployeeHomepageResponse(){
		ApplicationProgressStatuses = new ArrayList<>();
	}
		
	public static class ApplicationProgressStatus{
		Application application
		Job job	
		
		int currentProposalId
		Proposal currentProposal
		Proposal previousProposal
		
		String currentProposalStatus
		Boolean isCurrentProposalExpired
		Boolean isProposedToSessionUser
		String time_untilEmployerApprovalExpires
		
		List<String> messages
		
		int countJobWorkDays
		
		
		// properties set for the employee
		
	}
}
