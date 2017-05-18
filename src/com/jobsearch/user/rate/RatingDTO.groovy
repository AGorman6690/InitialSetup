package com.jobsearch.user.rate

import java.util.List

import com.fasterxml.jackson.annotation.JsonProperty
import com.jobsearch.model.Endorsement
import com.jobsearch.model.RateCriterion

public class RatingDTO {
	@JsonProperty("employeeId")
	int employeeId

	@JsonProperty("jobId")
	int jobId

	@JsonProperty("rateCriteria")
	List<RateCriterion> rateCriteria

	@JsonProperty("endorsements")
	List<Endorsement> endorsements
	
	@JsonProperty("value")
	double value

	@JsonProperty("comment")
	String comment
	

	
}
