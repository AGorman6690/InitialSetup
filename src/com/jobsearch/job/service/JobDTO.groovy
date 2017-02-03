package com.jobsearch.job.service

import java.util.List

import com.jobsearch.application.service.Application
import com.jobsearch.category.service.Category;
import com.jobsearch.model.FailedWageNegotiationDTO
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO
import com.jobsearch.model.Question
import com.jobsearch.user.rate.RatingDTO

public class JobDTO {
	 // **********************************************
	 // **********************************************
	 // The thought behind this DTO was to create a place to put all the
	 // miscellaneous information that is sometimes associated with a job object.
	 // The job class' property list is getting awfully long and confusing.
	 // So any info that needs to be bundled with a job, for whatever purpose,
	 // in order to display something to the user, I purpose putting the info here.
	 // Then the job class can only contain the info related to a job (i.e. the columns in the job table).
	 // **********************************************
	 // **********************************************

	 Job job
	 
	 Integer durationDays
	 double durationHours
	 List<WorkDay> workDays
	 String date_firstWorkDay
	 int months_workDaysSpan
	 double daysUntilStart
	 
	 List<FailedWageNegotiationDTO> failedWageNegotiationDtos
	 int countWageProposals_sent
	 int countWageProposals_received
	 
	 List<Question> questions
	 int newApplicationCount
	 List<JobSearchUser> applicants
	 List<Application> applications
	
	 List<JobSearchUser> employees
	 List<JobSearchUserDTO> employeeDtos
	 RatingDTO ratingDto
	 
	 List<Category> categories
	 
	 Double distanceFromFilterLocation

}
