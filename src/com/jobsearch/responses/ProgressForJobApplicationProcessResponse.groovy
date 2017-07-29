package com.jobsearch.responses;

import java.util.ArrayList;
import java.util.List;

import com.jobsearch.application.service.Application
import com.jobsearch.model.Proposal;
import com.jobsearch.model.Question;
import com.jobsearch.model.WorkDay;

public class ProgressForJobApplicationProcessResponse {
	
	public ProgressForJobApplicationProcessResponse(){
		ApplicationProgressStatuses = new ArrayList<>()
	}

	List<ApplicationProgressStatus> ApplicationProgressStatuses
	
	public static class ApplicationProgressStatus{
		Application application
		
		int currentProposalId
		Proposal currentProposal
		Proposal previousProposal
		
//		String currentProposedAmount
//		List<String> currentProposedDates
		String currentProposalStatus
		Boolean isCurrentProposalExpired
		Boolean isProposedToSessionUser
		String time_untilEmployerApprovalExpires
		
//		String previousProposedAmount
//		List<String> previousProposedDates
		String expirationTime
		
		String applicantName
		Double applicantRating
		int applicantId
		
		List<Question> questions
		List<Integer> answerOptionIds_Selected
	}
}
