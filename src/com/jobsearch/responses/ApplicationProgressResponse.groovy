package com.jobsearch.responses;

import java.util.ArrayList;
import java.util.List;

import com.jobsearch.model.Application
import com.jobsearch.model.Job
import com.jobsearch.model.Proposal;
import com.jobsearch.model.Question;
import com.jobsearch.model.WorkDay;

public class ApplicationProgressResponse {
	
	public ApplicationProgressResponse(){
		ApplicationProgressStatuses = new ArrayList<>()
	}

	List<ApplicationProgressStatus> ApplicationProgressStatuses
	Job job
//	int countJobWorkDays;
	
	public static class ApplicationProgressStatus{
		Application application
						
		Proposal currentProposal
		Proposal previousProposal
		
		String currentProposalLabel
		String previousProposalLabel
		
		String currentProposalStatus
		Boolean isCurrentProposalExpired
		boolean isProposedToSessionUser
		String time_untilEmployerApprovalExpires
		
		List<String> messages
		
//		String expirationTime
		
		// properties set for the employer
		String applicantName
		Double applicantRating
		int applicantId
		
		List<Question> questions
		List<Integer> answerOptionIds_Selected
		
		int countJobWorkDays
		
	}
}
