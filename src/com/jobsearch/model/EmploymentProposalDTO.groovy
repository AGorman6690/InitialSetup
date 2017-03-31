package com.jobsearch.model

import java.time.LocalDateTime;

class EmploymentProposalDTO {
	
	Integer applicationId
	Integer employmentProposalId
	Integer proposedByUserId
	Integer proposedToUserId	
	String amount
	LocalDateTime employerAcceptedDate
	LocalDateTime expirationDate
	
	Integer days_offerExpires
	Integer hours_offerExpires
	Integer minutes_offerExpires
	String time_untilEmployerApprovalExpires

	
	List<String> dateStrings_proposedDates
	Boolean isProposedToSessionUser

		
	Integer status
	public static Integer STATUS_SUBMITTED_BUT_NOT_VIEWED = -2
	public static Integer STATUS_VIEWED_BUT_NO_ACTION_TAKEN = -1
	public static Integer STATUS_COUNTERED = 0
	public static Integer STATUS_ACCEPTED = 1
	public static Integer STATUS_DECLINED = 2
	public static Integer STATUS_PENDING_APPLICANT_APPROVAL = 3;
	public static Integer STATUS_APPROVED_BY_APPLICANT = 4;

}
