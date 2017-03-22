package com.jobsearch.model

class WageProposal {
	
	int id
	int applicationId
	String amount
	int proposedByUserId
	int proposedToUserId	

	Integer status
	public static Integer STATUS_SUBMITTED_BUT_NOT_VIEWED = -2
	public static Integer STATUS_VIEWED_BUT_NO_ACTION_TAKEN = -1
	public static Integer STATUS_COUNTERED = 0
	public static Integer STATUS_ACCEPTED = 1
	public static Integer STATUS_DECLINED = 2
	public static Integer STATUS_PENDING_APPLICANT_APPROVAL = 3;
}
