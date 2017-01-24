package com.jobsearch.application.service

import java.util.List

import com.jobsearch.job.service.Job
import com.jobsearch.model.Answer;
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.Question
import com.jobsearch.model.WageProposal

public class Application {
	int applicationId
	int userId
	int jobId
	int hasBeenViewed
	String jobName
	Job job
	JobSearchUser applicant
	List<Question> questions
	List<WageProposal> wageProposals
	WageProposal wageProposal
	WageProposal currentWageProposal
	float currentDesiredWage
	List<Answer> answers


	//Status values:
	//0: submitted
	//1: declined
	//2: considered
	//3: accepted
	int status
	
	public static Integer STATUS_SUBMITTED = 0;
	public static Integer STATUS_DECLINED = 1;
	public static Integer STATUS_CONSIDERED = 2;
	public static Integer STATUS_ACCEPTED = 3;
}
