package com.jobsearch.model

import java.time.LocalDate

import org.codehaus.jackson.annotate.JsonProperty

import com.jobsearch.application.service.Application
import com.jobsearch.application.service.ApplicationDTO
import com.jobsearch.job.service.Job
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

public class WorkDayDto {
	
	WorkDay workDay
	
	Integer count_applicants
	Integer count_positionsFilled
	Integer count_totalPositions
	
	Boolean isProposed
	Job job_conflictingEmployment
	List<ApplicationDTO> applicationDtos_conflictingApplications
}
