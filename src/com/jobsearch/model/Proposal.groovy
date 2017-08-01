package com.jobsearch.model

import java.lang.invoke.DirectMethodHandle.StaticAccessor;
import java.time.LocalDateTime

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;;

public class Proposal {
	
	Integer applicationId
	Integer employmentProposalId
	Integer proposedByUserId
	Integer proposedToUserId	
	String amount
	LocalDateTime employerAcceptedDate
	LocalDateTime expirationDate
	Integer isNew
	List<String> proposedDates
	Integer flag_isCanceledDueToApplicantAcceptingOtherEmployment
	Integer flag_isCanceledDueToEmployerFillingAllPositions
	Integer flag_applicationWasReopened
	Integer flag_aProposedWorkDayWasRemoved
	Integer flag_aProposedWorkDayTimeWasEdited
	Integer flag_employerInitiatedContact
	Integer flag_employerAcceptedTheOffer
	
	// These flags are equal to column names in the wage_proposal table
	public static String FLAG_APPLICATION_WAS_REOPENED = "Flag_ApplicationWasReopened"
	public static String FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED = "Flag_AProposedWorkDayWasRemoved"
	public static String FLAG_A_PROPOSED_WORK_DAY_TIME_WAS_EDITED = "Flag_AProposedWorkDayTimeWasEdited"
	public static String FLAG_EMPLOYER_INITIATED_CONTACT = "Flag_EmployerInitiatedContact"
	public static String FLAG_IS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS = "Flag_IsCanceledDueToEmployerFillingAllPositions"
	public static String FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT = "Flag_IsCanceledDueToApplicantAcceptingOtherEmployment"
	public static String FLAG_EMPLOYER_ACCEPTED_THE_OFFER = "Flag_EmployerAcceptedTheOffer";


}