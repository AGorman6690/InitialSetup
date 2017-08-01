package com.jobsearch.responses

import com.jobsearch.application.service.Application
import com.jobsearch.dtos.CompletedJobDto
import com.jobsearch.dtos.ProfileInfoDto;
import com.jobsearch.job.service.Job
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.Proposal

class ViewEmployeeHomepageResponse {

	List<ApplicationProgressStatus> ApplicationProgressStatuses
	long countProposals_waitingOnYou;
	long countProposals_waitingOnYou_new;
	long countProposals_waitingOnOther;
	long countJobs_employment;
	ProfileInfoDto profileInfoDto;
	
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
