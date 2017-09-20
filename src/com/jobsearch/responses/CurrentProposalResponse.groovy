package com.jobsearch.responses

import java.lang.invoke.DirectMethodHandle.StaticAccessor
import java.util.Arrays;

import com.jobsearch.model.Job
import com.jobsearch.model.Proposal
import com.jobsearch.model.WorkDay
import com.jobsearch.model.WorkDayDto;

// *********************************************************************************
// Refactor : this needs to be harmonized with InitMakeOfferResponse
// *********************************************************************************

public class CurrentProposalResponse {
	Proposal currentProposal
	Job job
//	List<WorkDay> jobWorkDays
	String date_firstWorkDay
	int monthSpan_allWorkDays
	String timeUntilStart
	String time_untilEmployerApprovalExpires
//	List<JobWorkDay> jobWorkDays
	int jobWorkDayCount;
	Integer proposeToUserId
	 
	public static class JobWorkDay{
	

	}
}
