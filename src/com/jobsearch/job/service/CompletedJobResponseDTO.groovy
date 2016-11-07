package com.jobsearch.job.service

import com.jobsearch.model.Endorsement
import java.util.List

class CompletedJobResponseDTO {
	Job job
	String comment
	double rating
	List<Endorsement> endorsements
}
