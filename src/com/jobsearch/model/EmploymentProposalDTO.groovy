package com.jobsearch.model

import java.lang.invoke.DirectMethodHandle.StaticAccessor;
import java.time.LocalDateTime

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;;

class EmploymentProposalDTO {
	
	public EmploymentProposalDTO(EmploymentProposalDTO proposalBeingRespondedTo) {
			this.setProposedByUserId(proposalBeingRespondedTo.getProposedToUserId());
			this.setProposedToUserId(proposalBeingRespondedTo.getProposedByUserId());
			this.setStatus(WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED);
			this.setApplicationId(proposalBeingRespondedTo.getApplicationId());
	}
	public EmploymentProposalDTO() {
		// TODO Auto-generated constructor stub
	}
	Integer applicationId
	Integer employmentProposalId
	Integer proposedByUserId
	Integer proposedToUserId	
	String amount
	LocalDateTime employerAcceptedDate
	LocalDateTime expirationDate
	
	String dateString_expirationDate
	String timeString_expirationTime
	
	Boolean isExpired
	Boolean isAcceptingOffer
	Boolean isDeclining
	
	Integer flag_isCanceledDueToApplicantAcceptingOtherEmployment	
	Integer flag_isCanceledDueToEmployerFillingAllPositions
	Integer flag_applicationWasReopened
	Integer flag_aProposedWorkDayWasRemoved
	Integer flag_aProposedWorkDayTimeWasEdited
	Integer flag_employerInitiatedContact
	Integer flag_employerAcceptedTheOffer
	
	Integer days_offerExpires
	Integer hours_offerExpires
	Integer minutes_offerExpires
	String time_untilEmployerApprovalExpires


//	List<WorkDayDto> workDayDtos_proposed
	List<String> dateStrings_proposedDates
	Boolean isProposedToSessionUser
	
	List<WorkDayDto> proposedWorkDays
	
	// *******************************************************************
	// *******************************************************************
	// I believe these statuses can be eliminated not that IsCurrentProposal is a column,
	// and 
	// *******************************************************************
	// *******************************************************************
	
	// These flags are equal to column names in the wage_proposal table
	public static String FLAG_APPLICATION_WAS_REOPENED = "Flag_ApplicationWasReopened"
	public static String FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED = "Flag_AProposedWorkDayWasRemoved"
	public static String FLAG_A_PROPOSED_WORK_DAY_TIME_WAS_EDITED = "Flag_AProposedWorkDayTimeWasEdited"
	public static String FLAG_EMPLOYER_INITIATED_CONTACT = "Flag_EmployerInitiatedContact"
	public static String FLAG_IS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS = "Flag_IsCanceledDueToEmployerFillingAllPositions"
	public static String FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT = "Flag_IsCanceledDueToApplicantAcceptingOtherEmployment"
	public static String FLAG_EMPLOYER_ACCEPTED_THE_OFFER = "Flag_EmployerAcceptedTheOffer";
	
	Integer status
	public static Integer STATUS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS = -4
	public static Integer STATUS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHER_EMPLOYMENT = -3
	public static Integer STATUS_SUBMITTED_BUT_NOT_VIEWED = -2
	public static Integer STATUS_VIEWED_BUT_NO_ACTION_TAKEN = -1
	public static Integer STATUS_COUNTERED = 0
	public static Integer STATUS_ACCEPTED = 1
	public static Integer STATUS_DECLINED = 2
	public static Integer STATUS_PENDING_APPLICANT_APPROVAL = 3;
	public static Integer STATUS_APPROVED_BY_APPLICANT = 4;

}
