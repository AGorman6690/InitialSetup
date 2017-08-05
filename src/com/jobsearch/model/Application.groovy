package com.jobsearch.model

import java.awt.TexturePaintContext.Int;
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.List

import com.jobsearch.model.Job
import com.jobsearch.model.Answer;
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.Question
import com.jobsearch.model.WageProposal

public class Application {
	
	int applicationId
	int userId
	int jobId
	int hasBeenViewed
	int isNew
	int isAccepted
	int isOpen
	
	int flag_employerInitiatedContact
	int flag_closedDueToAllPositionsFilled
	int flag_applicantAcknowledgedAllPositionsAreFilled
	
	// ******************************
	// Do away with these.
	// They are moving to the employment proposal dto
	// *******************************
	LocalDateTime employerAcceptedDate
	LocalDateTime expirationDate

	// *************************************************************************
	// *************************************************************************
	// This status property is evolving into more than a simple "status".
	// Should the status be replaced by several boolean properties????
	// I'm beginning to think so: 
	// isOpen
	// isConsidered
	// isProposedByEmployer
	// isCanceledDueToTimeConflict
	// isAccepted
	// *************************************************************************
	// *************************************************************************
	
	int status	
//	public static Integer STATUS_DOES_NOT_EXIST = -999;
	public static Integer STATUS_PROPOSED_BY_EMPLOYER = -1;	
	public static Integer STATUS_SUBMITTED = 0;
	public static Integer STATUS_DECLINED = 1;
	public static Integer STATUS_CONSIDERED = 2;
	public static Integer STATUS_ACCEPTED = 3;
	public static Integer STATUS_WAITING_FOR_APPLICANT_APPROVAL = 4;
	public static Integer STATUS_CANCELLED_DUE_TO_TIME_CONFLICT = 5;
	public static Integer STATUS_CANCELLED_DUE_TO_EMPLOYER_FILLED_ALL_POSITIONS = 6;
	
	public static String FLAG_EMPLOYER_INITIATED_CONTACT = "Flag_EmployerInitiatedContact";
	public static String FLAG_CLOSED_DUE_TO_ALL_POSITIONS_FILLED = "Flag_ClosedDueToAllPositionsFilled"
	public static String FLAG_APPLICANT_ACKNOWLEDGED_ALL_POSITIONS_ARE_FILLED = "Flag_ApplicantAcknowledgedAllPositionsAreFilled"
}


