package com.jobsearch.responses

import com.jobsearch.model.Job
import com.jobsearch.model.Proposal

// ***********************************************
// This might not be the best name as it also includes properties for an
// employer to make the initial proposal.
// ***********************************************

public class CurrentProposalResponse {
	Proposal currentProposal
	Job job
	String date_firstWorkDay
	int monthSpan_allWorkDays
	String timeUntilStart
	String time_untilEmployerApprovalExpires
	int jobWorkDayCount;
	Integer proposeToUserId
	
	// these are for the employer when he is making the initial proposal
	List<Job> employerOpenJobs
	int userId_makeInitialProposalTo
	String userName_makeInitialProposalTo
	boolean hasUserAlreadyApplied
	Integer jobId_makeInitialOfferFor
}
