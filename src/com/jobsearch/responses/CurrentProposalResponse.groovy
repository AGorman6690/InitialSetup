package com.jobsearch.responses

import java.lang.invoke.DirectMethodHandle.StaticAccessor
import java.util.Arrays;

import com.jobsearch.model.Job
import com.jobsearch.model.Proposal
import com.jobsearch.model.WorkDay
import com.jobsearch.model.WorkDayDto;

public class CurrentProposalResponse {
	Proposal currentProposal
	Job job
//	List<WorkDay> jobWorkDays
	String date_firstWorkDay
	int monthSpan_allWorkDays
	String timeUntilStart
	String time_untilEmployerApprovalExpires
//	List<JobWorkDay> jobWorkDays
	
	public static class JobWorkDay{
	

	}
}
