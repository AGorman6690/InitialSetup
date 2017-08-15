package com.jobsearch.request

import java.util.List;

public class FindEmployeesRequest {
	List<String> dates
	Integer minimumRating
	Integer minimumJobsCompleted
	Integer jobId_findEmployeesFor
	Float lat
	Float lng
	String address
	Double radius
}
