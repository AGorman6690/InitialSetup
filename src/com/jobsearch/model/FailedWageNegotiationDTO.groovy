package com.jobsearch.model

import com.jobsearch.job.service.Job;

class FailedWageNegotiationDTO {
	WageProposal failedWageProposal
	JobSearchUser otherUser
	Job job
}
