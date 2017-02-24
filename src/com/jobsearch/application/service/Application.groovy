package com.jobsearch.application.service

import java.time.LocalDateTime
import java.time.LocalTime
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
	LocalDateTime employerAcceptedDate
	LocalDateTime expirationDate
	
	int status	
	public static Integer STATUS_DOES_NOT_EXIST = -999;
	public static Integer STATUS_PROPOSED_BY_EMPLOYER = -1;
	public static Integer STATUS_SUBMITTED = 0;
	public static Integer STATUS_DECLINED = 1;
	public static Integer STATUS_CONSIDERED = 2;
	public static Integer STATUS_ACCEPTED = 3;
	public static Integer STATUS_WAITING_FOR_APPLICANT_APPROVAL = 4;
	public static Integer STATUS_CANCELLED_DUE_TO_TIME_CONFLICT = 5;
}
