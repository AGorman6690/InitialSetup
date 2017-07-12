package com.jobsearch.model


public class EmployeeSearch {
//	JobDTO jobDto;
//	Integer jobId_excludeApplicantsOfThisJob;
//	Integer jobId_onlyIncludeApplicantsOfThisJob_butExcludeApplicantsOnTheseWorkDays;
	Integer minimumRating
	Integer minimumJobsCompleted
	List<String> workDays
	Integer jobId_findEmployeesFor
	Float lat
	Float lng
	String address
	Double radius
}
