package com.jobsearch.application.service

import com.jobsearch.job.service.Job
import com.jobsearch.model.WageProposal

class ApplicationDTO {
	
	Application application
	WageProposal currentWageProposal
	List<WageProposal> wageProposals
	Job job
	int newStatus

}
