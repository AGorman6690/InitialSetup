package com.jobsearch.model

import com.jobsearch.application.service.Application
import com.jobsearch.job.service.FindJobFilterDTO
import com.jobsearch.job.service.Job
import com.jobsearch.job.service.JobDTO
import com.jobsearch.user.rate.RatingDTO
import com.jobsearch.category.service.Category
import com.jobsearch.category.service.CategoryDTO
import java.util.List


class JobSearchUserDTO {

	JobSearchUser user
//	
	RatingDTO rating
	List<FindJobFilterDTO> savedFindJobFilters;
	List<JobDTO> jobDtos_jobsCompleted;
	List<String> availableDays;
	double wage
//	
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
	
}
