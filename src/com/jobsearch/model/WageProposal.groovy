package com.jobsearch.model

class WageProposal {
	int id
	int applicationId
	double amount
	int proposedByUserId
	int proposedToUserId
	
	//-1: No action taken;
	//0: Countered;
	//1: Accepted;
	//2: Declined;
	int status
}
