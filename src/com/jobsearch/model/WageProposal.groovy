package com.jobsearch.model

class WageProposal {
	int id
	int applicationId
	double amount
	int proposedByUserId
	int proposedToUserId
	
	// -2: Submitted and not yet viewed
	// -1: Viewed, but no action taken;
	// 0: Countered;
	// 1: Accepted;
	// 2: Declined;
	int status

	// *********************************************
	// *********************************************
	// NOTE: The SUBMITTED_BUT_NOT_VIEWED status only pertains to the "employer" view.
	// 
	// *********************************************
	// *********************************************
	public static Integer STATUS_SUBMITTED_BUT_NOT_VIEWED = -2
	public static Integer STATUS_VIEWED_BUT_NO_ACTION_TAKEN = -1
	public static Integer STATUS_COUNTERED = 0
	public static Integer STATUS_ACCEPTED = 1
	public static Integer STATUS_DECLINED = 2
	public static Integer STATUS_PENDING_APPLICANT_APPROVAL = 3;
}
