package com.jobsearch.model


class EmploymentProposalDTO {
	
	Integer applicationId
	Integer employmentProposalId
	Integer proposedByUserId
	Integer proposedToUserId	
	String amount
	
	Integer days_offerExpires
	Integer hours_offerExpires
	Integer minutes_offerExpires
	
	List<String> dateStrings_proposedDates
	
	
	Integer context
	public static Integer CONTEXT_INITIATE = 0
	public static Integer CONTEXT_COUNTER = 1
	public static Integer CONTEXT_ACCEPT_BY_EMPLOYER = 2
	public static Integer CONTEXT_APPROVE_BY_EMPLOYEE = 3
	public static Integer CONTEXT_DECLINE	 = 4
	
		
	Integer status
	public static Integer STATUS_SUBMITTED_BUT_NOT_VIEWED = -2
	public static Integer STATUS_VIEWED_BUT_NO_ACTION_TAKEN = -1
	public static Integer STATUS_COUNTERED = 0
	public static Integer STATUS_ACCEPTED = 1
	public static Integer STATUS_DECLINED = 2
	public static Integer STATUS_PENDING_APPLICANT_APPROVAL = 3;
	public static Integer STATUS_APPROVED_BY_APPLICANT = 4;

}
