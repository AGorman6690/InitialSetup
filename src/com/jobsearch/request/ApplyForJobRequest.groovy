package com.jobsearch.request

import com.jobsearch.model.Answer;

public class ApplyForJobRequest {
	int jobId
	String proposedWage
	List<String> proposedDates
	List<Answer> answers
}
