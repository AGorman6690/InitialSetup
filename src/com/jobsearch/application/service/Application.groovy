package com.jobsearch.application.service

import java.util.List;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;

class Application {
	int applicationId
	int userId
	int jobId
	int hasBeenViewed
	String jobName
	Job job
	JobSearchUser applicant
	List<Question> questions

	//Status values:
	//0: submitted
	//1: declined
	//2: considered
	//3: accepted
	int status
}
