package com.jobsearch.model

import com.jobsearch.application.service.Application
import com.jobsearch.application.service.ApplicationDTO
import com.jobsearch.job.service.Job
import com.jobsearch.job.web.FindJobFilterDTO;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.user.rate.RatingDTO
import com.jobsearch.category.service.Category
import com.jobsearch.category.service.CategoryDTO
import java.util.List


class JobSearchUserDTO {

	JobSearchUser user
//	
	RatingDTO ratingDto
	List<FindJobFilterDTO> savedFindJobFilters;
	
	List<String> availableDays;
	List<String> unavailableDays;
	Integer count_availableDays_perFindEmployeesSearch
	
	String totalPayment
	EmploymentProposalDTO acceptedProposal
	
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
	List<CategoryDTO> categoryDtos_jobsCompleted
	
	Boolean isInvalidNewUser
	Boolean isInvalidFirstName
	Boolean isInvalidLastName
	Boolean isInvalidPassword
	Boolean isInvalidMatchingPassword
	Boolean isInvalidEmail_format
	Boolean isInvalidEmail_duplicate
	Boolean isInvalidMatchingEmail
	Boolean isInvalidProfile
	
	JobSearchUserDTO(){
		this.setRatingDto(new RatingDTO());
	}
	
}
