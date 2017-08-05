package com.jobsearch.model

import java.time.LocalDate

import org.codehaus.jackson.annotate.JsonProperty
import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import com.jobsearch.model.Application
import com.jobsearch.dtos.ApplicationDTO
import com.jobsearch.model.Job
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

public class WorkDayDto {
	
	WorkDay workDay
	
	Integer count_applicants
	Integer count_positionsFilled
	Integer count_totalPositions
	
	Boolean hasConflictingEmployment
	Boolean hasOpenPositions
	Boolean isProposed
	Boolean isAccepted
	
	Job job_conflictingEmployment
	List<ApplicationDTO> applicationDtos_conflictingApplications
	
	Object date
	
	WorkDayDto(){
		this.workDay = new WorkDay();
	}
}
 