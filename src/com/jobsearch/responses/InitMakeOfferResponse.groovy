package com.jobsearch.responses

import com.jobsearch.model.Job;
import com.jobsearch.model.WorkDay

// *********************************************************************************
// Refactor : this needs to be harmonized with CurrentProposalResponse
// *********************************************************************************

public class InitMakeOfferResponse {

	Integer proposeToUserId	
	Job job
	List<WorkDay> jobWorkDays
	String date_firstWorkDay
	int monthSpan_allWorkDays
}
