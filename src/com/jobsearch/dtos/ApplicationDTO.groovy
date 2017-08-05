package com.jobsearch.dtos

import com.jobsearch.job.web.JobDTO
import com.jobsearch.model.Answer
import com.jobsearch.model.Application
import com.jobsearch.model.EmploymentProposalDTO
import com.jobsearch.model.JobSearchUserDTO
import com.jobsearch.model.Question
import com.jobsearch.model.WageProposal

class ApplicationDTO {
	
	Application application
	
	List<Question> questions
	List<Answer> answers
	List<Integer> answerOptionIds_Selected	
	
	EmploymentProposalDTO previousProposal
	EmploymentProposalDTO employmentProposalDto
	
	String currentProposalStatus
	WageProposal currentWageProposal
	List<WageProposal> wageProposals
	
	// ******************************
	// Can this be removed????
	// Phase out all WagePropsal objects and replace with EmploymentProposalDTO
	// ******************************
	WageProposal wageProposal
		
	JobDTO jobDto
	int jobId
	
	List<String> messages
	
	JobSearchUserDTO applicantDto
	int applicantId
		
	Integer count_conflictingApplications
	List<ApplicationDTO> applicationDtos_conflicting_willBeRemoved
	List<ApplicationDTO> applicationDtos_conflicting_willBeSentBackToEmployer
	List<ApplicationDTO> applicationDtos_conflicting_willBeModifiedButRemainAtEmployer
	
	String time_untilEmployerApprovalExpires

	int newStatus
	
	
	// ****************************************************************
	// These have been added to EmploymentProposalDTO.
	// Remove them.
	// ****************************************************************
//	Integer days_offerExpires
//	Integer hours_offerExpires
//	Integer minutes_offerExpires
	
	// ******************************************************.
	// Rename this to: dateStrings_currentlyProposedWorkDays
	// ******************************************************	
	List<String> dateStrings_availableWorkDays
//	List<String> dateStrings_currentlyProposedWorkDays
		
	List<String> dateStrings_unavailableWorkDays
	

	
	ApplicationDTO(){
		this.applicantDto = new JobSearchUserDTO();
		this.jobDto = new JobDTO();
		this.application = new Application();
		this.applicationDtos_conflicting_willBeRemoved = new ArrayList<ApplicationDTO>();
		this.applicationDtos_conflicting_willBeSentBackToEmployer = new ArrayList<ApplicationDTO>();
		this.applicationDtos_conflicting_willBeModifiedButRemainAtEmployer = new ArrayList<ApplicationDTO>();
	}


}
