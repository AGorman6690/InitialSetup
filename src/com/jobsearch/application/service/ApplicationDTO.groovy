package com.jobsearch.application.service

import java.util.List;

import com.jobsearch.job.service.Job
import com.jobsearch.job.service.JobDTO
import com.jobsearch.model.Answer
import com.jobsearch.model.JobSearchUserDTO
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal

class ApplicationDTO {
	
	Application application
	
	List<Question> questions
	List<Answer> answers
	List<Integer> answerOptionIds_Selected
	
	WageProposal currentWageProposal
	List<WageProposal> wageProposals
	WageProposal wageProposal
		
	JobDTO jobDto
	int jobId
	
	JobSearchUserDTO applicantDto
	int applicantId
		
	List<ApplicationDTO> applicationDtos_conflicting
	
	String time_untilEmployerApprovalExpires
	
	int newStatus
	
	Integer days_offerExpires
	Integer hours_offerExpires
	Integer minutes_offerExpires
	
	ApplicationDTO(){
		this.applicantDto = new JobSearchUserDTO();
		this.jobDto = new JobDTO();
	}

}
