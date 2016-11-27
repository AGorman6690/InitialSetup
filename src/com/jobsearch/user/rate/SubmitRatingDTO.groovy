package com.jobsearch.user.rate

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobsearch.model.RateCriterion;

class SubmitRatingDTO {

	@JsonProperty("employeeId")
	int employeeId

	@JsonProperty("rateCriteria")
	List<RateCriterion> rateCriteria

	@JsonProperty("endorsementCategoryIds")
	List<Integer> endorsementCategoryIds
	
	@JsonProperty("commentString")
	String commentString
}
