package com.jobsearch.application.service

import java.util.List

import com.jobsearch.job.service.Job
import com.jobsearch.model.Answer;
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.Question
import com.jobsearch.model.WageProposal

public class Application {
	
	// ********************************************
	// ********************************************
	// Can most of these properties be moved to the ApplicationDTO???
	// Consider removing "hasBeenViewed" and adding another status.
	// ********************************************
	// ********************************************
	
	int applicationId
	int userId
	int jobId
	int hasBeenViewed
	
	String jobName
	Job job
	JobSearchUser applicant
	List<Question> questions
	List<Integer> answerOptionIds_Selected
	List<WageProposal> wageProposals
	WageProposal wageProposal
	WageProposal currentWageProposal
	float currentDesiredWage
	List<Answer> answers
	
	int status	
	public static Integer STATUS_SUBMITTED = 0;
	public static Integer STATUS_DECLINED = 1;
	public static Integer STATUS_CONSIDERED = 2;
	public static Integer STATUS_WAITING_FOR_APPLICANT_APPROVAL = 2.5;
	public static Integer STATUS_ACCEPTED = 3;
}
