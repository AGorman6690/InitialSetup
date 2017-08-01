package com.jobsearch.dtos

import java.util.List;

import com.jobsearch.model.Endorsement;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.RateCriterion;

public class ProfileRatingDto {
	JobSearchUser user
	List<RateCriterion> rateCriteria
	List<Endorsement> endorsements
	Double overallRating
	String comment
}
