package com.jobsearch.dtos;

import java.util.List;

import com.jobsearch.model.Endorsement;
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.RateCriterion;

public class ProfileInfoDto {

	JobSearchUser user
	ProfileRatingDto profileRatingDto
	Boolean doesUserHaveEnoughDataToCalculateRating
	List<CompletedJobDto> completedJobsDtos;
}
