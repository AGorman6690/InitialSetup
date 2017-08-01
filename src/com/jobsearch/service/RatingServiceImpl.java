package com.jobsearch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.dtos.ProfileInfoDto;
import com.jobsearch.dtos.ProfileRatingDto;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.repository.RatingRepository;
import com.jobsearch.utilities.MathUtility;;

@Service
public class RatingServiceImpl {
	
	@Autowired
	RatingRepository repository;
	
	public Double getRating_byJobAndUser(Integer jobId, int userId) {
		return repository.getRating_byJobAndUser(jobId, userId);
	}
	
	public ProfileRatingDto getProfileRatingDto(JobSearchUser user) {

		ProfileRatingDto profileRatingDto = new ProfileRatingDto();
		profileRatingDto.setRateCriteria(getRateCriteria(user));
		for (RateCriterion rateCriterion : profileRatingDto.getRateCriteria()) {
			rateCriterion.setValue(getRatingValue_byCriteriaAndUser(
					rateCriterion.getRateCriterionId(), user.getUserId()));
			if (rateCriterion.getValue() != null)
				rateCriterion.setStringValue(String.format("%.1f", rateCriterion.getValue()));
		}
		profileRatingDto.setOverallRating(getOverallRating(user.getUserId()));
		return profileRatingDto;
	}
	
	private List<RateCriterion> getRateCriteria(JobSearchUser user) {
		if (user.getProfileId() == Profile.PROFILE_ID_EMPLOYEE)
			return getRatingCriteia_toRateEmployee();
		else
			return getRatingCriteia_toRateEmployer();
	}

	public List<RateCriterion> getRatingCriteia_toRateEmployee() {
		return repository.getRatingCriteia_toRateEmployee();
	}
	
	public List<RateCriterion> getRatingCriteia_toRateEmployer() {
		return repository.getRateCriteria_toRateEmployer();
	}
	
	public Double getRatingValue_byCriteriaAndUser(Integer rateCriterionId, int userId) {
		return repository.getRatingValue_byCriteriaAndUser(rateCriterionId, userId);
	}
	
	public Double getOverallRating(int userId) {
		return MathUtility.round(repository.getOverallRating(userId), 1, 0);
	}
	
	

}
