package com.jobsearch.model

import java.lang.invoke.DirectMethodHandle.StaticAccessor;
import java.time.LocalDateTime

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;;

public class Proposal {
	
	Integer applicationId
	Integer proposalId
	Integer proposedByUserId
	Integer proposedToUserId	
	String amount
	LocalDateTime employerAcceptedDate
	LocalDateTime expirationDate
	Integer isNew
	Integer isDeclined
	Integer isCurrentProposal
	List<String> proposedDates
	Integer flag_isCanceledDueToApplicantAcceptingOtherEmployment
	Integer flag_isCanceledDueToEmployerFillingAllPositions
	Integer flag_isCreatedDueToApplicantAcceptingOtherEmployment
	Integer flag_isCreatedDueToEmployerFillingAllPositions
	Integer flag_applicationWasReopened
	Integer flag_aProposedWorkDayWasRemoved
	Integer flag_aProposedWorkDayTimeWasEdited
	Integer flag_employerInitiatedContact
	Integer flag_employerAcceptedTheOffer
	Integer flag_acknowledgedIsDeclined
	Integer flag_hasExpired
	
	// These flags are equal to column names in the wage_proposal table
	public static String FLAG_APPLICATION_WAS_REOPENED = "Flag_ApplicationWasReopened"
	public static String FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED = "Flag_AProposedWorkDayWasRemoved"
	public static String FLAG_A_PROPOSED_WORK_DAY_TIME_WAS_EDITED = "Flag_AProposedWorkDayTimeWasEdited"
	public static String FLAG_EMPLOYER_INITIATED_CONTACT = "Flag_EmployerInitiatedContact"
	public static String FLAG_IS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS = "Flag_IsCanceledDueToEmployerFillingAllPositions"
	public static String FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT = "Flag_IsCanceledDueToApplicantAcceptingOtherEmployment"
	public static String FLAG_EMPLOYER_ACCEPTED_THE_OFFER = "Flag_EmployerAcceptedTheOffer";
	public static String FLAG_IS_CREATED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS = "Flag_IsCreatedDueToEmployerFillingAllPositions"
	public static String FLAG_IS_CREATED_DUE_TO_APPLICANT_ACCEPTING_OTHER_EMPLOYMENT = "Flag_IsCreatedDueToApplicantAcceptingOtherEmployment"
	public static String FLAG_ACKNOWLEDGED_IS_DECLINED = "Flag_AcknowledgedIsDeclined";
	public static String FLAG_HAS_EXPIRED = "Flag_HasExpired";
	
	public Proposal(){		
	}
	public Proposal(Proposal prposal) {
		this.setProposedByUserId(prposal.getProposedToUserId());
		this.setProposedToUserId(prposal.getProposedByUserId());
		this.setApplicationId(prposal.getApplicationId());
	}


}
