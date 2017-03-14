package com.jobsearch.model

import java.awt.TexturePaintContext.Int

import org.hibernate.dialect.Ingres10Dialect;;

class EmploymentProposalDTO {
	
	int applicationId
	int employmentProposalId
	int proposedByUserId
	int proposedToUserId	
	String amount
	
	List<String> dateStrings_proposedDates
		
	int status
	public static Integer STATUS_SUBMITTED_BUT_NOT_VIEWED = -2
	public static Integer STATUS_VIEWED_BUT_NO_ACTION_TAKEN = -1
	public static Integer STATUS_COUNTERED = 0
	public static Integer STATUS_ACCEPTED = 1
	public static Integer STATUS_DECLINED = 2
	public static Integer STATUS_PENDING_APPLICANT_APPROVAL = 3;

}