package com.jobsearch.request

import com.jobsearch.model.Proposal

public class RespondToProposalRequest {
	Proposal proposal
	Integer days_offerExpires
	Integer hours_offerExpires
	Integer minutes_offerExpires
	Boolean isRespondingToAnExpiredProposal
}
