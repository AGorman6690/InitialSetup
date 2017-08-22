package com.jobsearch.job.web

import com.jobsearch.dtos.ApplicationDTO
import com.jobsearch.model.Application
import com.jobsearch.model.Job
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.JobSearchUserDTO
import com.jobsearch.model.Question
import com.jobsearch.model.Skill
import com.jobsearch.model.WorkDay
import com.jobsearch.model.WorkDayDto


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
	 
	 Long milliseconds_startDate
	 Long milliseconds_endDate
	 
	 Integer durationDays
	 double durationHours
	 
	 // **************************************************************
	 // DO AWAY WITH THIS.
	 // WorkDayDtos is suffice.
	 List<WorkDay> workDays
	 // **************************************************************

	 // ****************************************************
	 // ****************************************************
	 // Remove the work day dtos. place them in the employment proposal Dto.
	 // these dtos are always in the context of a proposal
	 List<WorkDayDto> workDayDtos
	 // ****************************************************
	 // ****************************************************
	 
	 WorkDayDto workDayDto;
	 
	 Boolean areAllTimesTheSame
	 String date_firstWorkDay
	 int months_workDaysSpan
	 double daysUntilStart
	 String timeUntilStart
	 
	 int countWageProposals_sent
	 int countWageProposals_received
	 int countWageProposals_received_new
	 int countProposals_expired
	 
	 int countApplications_total
	 int countApplications_new
	 int countApplications_received
	 int countApplications_declined
	 
	 int countEmployees_hired
	 
	 List<Question> questions
	 List<JobSearchUser> applicants
	 List<Application> applications
	 Application application
	 Integer applicationStatus
	 
	 List<ApplicationDTO> applicationDtos
	 
	 List<JobSearchUser> employees
	 List<JobSearchUserDTO> employeeDtos
	 List<JobSearchUser> employees_whoLeft
	 
	 List<JobSearchUserDTO> userDtos_applicants
	 
	 JobSearchUserDTO employerDto

//	 List<RatingDTO> ratingDtos
	 Double ratingValue_overall
	 List<String> comments
	 
	 
	 Double distance
	 Double distanceFromFilterLocation
	 
	 List<Skill> skills
	 List<Skill> skillsDesired
	 List<Skill> skillsRequired
	 
	 int availabilityStatus
	 public static Integer AVAILABILITY_STATUS_NONE_DUE_TO_AVAILABILITY_NOT_SET = 0
	 public static Integer AVAILABILITY_STATUS_NONE_DUE_TO_EMPLOYMENT = 1
	 public static Integer AVAILABILITY_STATUS_PARTIAL_DUE_TO_AVAILABILITY_NOT_SET = 2
	 public static Integer AVAILABILITY_STATUS_PARTIAL_DUE_TO_EMPLOYMENT = 3
	 public static Integer AVAILABILITY_STATUS_COMPLETELY = 4
	 
	 JobDTO(){
		 this.job = new Job();	
		 this.workDays = new ArrayList<WorkDay>();	 
		 this.workDayDtos = new ArrayList<WorkDayDto>();
		 this.employerDto = new JobSearchUserDTO();		  
	 }


}
