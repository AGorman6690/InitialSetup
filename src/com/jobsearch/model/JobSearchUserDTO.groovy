package com.jobsearch.model

import com.jobsearch.dtos.ApplicationDTO
import com.jobsearch.job.web.JobDTO


class JobSearchUserDTO {

	JobSearchUser user
//	
	
	
	List<String> availableDays;
	List<String> unavailableDays;
	Integer count_availableDays_perFindEmployeesSearch
	
	String totalPayment

	
	ApplicationDTO applicationDto
	
	long countApplications_open;
	long countProposals_waitingOnYou;
	long countProposals_waitingOnYou_new;
	long countProposals_waitingOnOther;
	long countJobs_employment;
	
	List<JobDTO> jobDtos_jobsCompleted;
	int count_jobsCompleted;
//	
	
	Double ratingValue_overall
	
	Boolean isInvalidNewUser
	Boolean isInvalidFirstName
	Boolean isInvalidLastName
	Boolean isInvalidPassword
	Boolean isInvalidMatchingPassword
	Boolean isInvalidEmail_format
	Boolean isInvalidEmail_duplicate
	Boolean isInvalidMatchingEmail
	Boolean isInvalidProfile
	
}
