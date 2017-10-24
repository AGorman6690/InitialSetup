package com.jobsearch.request

import com.jobsearch.model.Proposal
import com.jobsearch.request.proposal.BaseProposalRequest

public class RespondToProposalRequest extends BaseProposalRequest {
	Proposal proposal
//	Integer days_offerExpires
//	Integer hours_offerExpires
//	Integer minutes_offerExpires
	Boolean isRespondingToAnExpiredProposal
	
	Integer applicationId
//	Boolean expiresFromNow
	
	// why is this a string?
	String amount
	List<String> proposedDates
	
}
