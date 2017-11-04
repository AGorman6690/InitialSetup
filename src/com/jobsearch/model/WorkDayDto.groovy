package com.jobsearch.model

import com.jobsearch.dtos.ApplicationDTO

public class WorkDayDto {
	
	WorkDay workDay
	
	Integer count_applicants
	Integer count_positionsFilled
	Integer count_totalPositions
	
	Boolean hasConflictingEmployment
	Boolean hasOpenPositions
	Boolean isProposed
	Boolean isAccepted
	Boolean isComplete
	
	Job job_conflictingEmployment
	List<ApplicationDTO> applicationDtos_conflictingApplications
	
	Object date
	
	WorkDayDto(){
		this.workDay = new WorkDay();
	}
}
 