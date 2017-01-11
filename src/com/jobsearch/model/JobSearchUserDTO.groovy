package com.jobsearch.model

import com.jobsearch.application.service.Application
import com.jobsearch.job.service.CompletedJobResponseDTO
import com.jobsearch.job.service.Job
import com.jobsearch.user.rate.RatingDTO
import com.jobsearch.category.service.Category
import java.util.List


class JobSearchUserDTO {
	JobSearchUser user
//	List<RateCriterion> ratingResults
//	List<Endorsement> endorsements
	RatingDTO rating
}
