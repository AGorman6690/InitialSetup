package com.jobsearch.job.service

import java.util.List
import com.jobsearch.model.Endorsement

class CompletedJobResponseDTO {
	Job job
	String comment
	double rating
	List<Endorsement> endorsements
}
