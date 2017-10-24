package com.jobsearch.request

import com.jobsearch.model.Proposal
import com.jobsearch.request.proposal.BaseProposalRequest

public class MakeInitialOfferByEmployerRequest extends BaseProposalRequest {
	int jobId
	int proposeToUserId
	String amount
	List<String> proposedDates
//	Integer days_offerExpires
//	Integer hours_offerExpires
//	Integer minutes_offerExpires
//	Boolean isRespondingToAnExpiredProposal
//	Boolean expiresFromNow
}
