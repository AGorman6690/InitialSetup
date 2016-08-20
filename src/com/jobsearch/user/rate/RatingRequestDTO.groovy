package com.jobsearch.user.rate

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobsearch.model.CommentDTO;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.RateCriterion;

class RatingRequestDTO {
	@JsonProperty("employeeId")
	int employeeId

	@JsonProperty("jobId")
	int jobId

	@JsonProperty("rateCriteria")
	List<RateCriterion> rateCriteria

	@JsonProperty("endorsements")
	List<Endorsement> endorsements

	@JsonProperty("comment")
	CommentDTO comment
}
