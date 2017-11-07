package com.jobsearch.request

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobsearch.model.RateCriterion;

public class SubmitRatingRequest {
	Integer jobId
	Integer userId_ratee
	List<RateCriterion> rateCriteria
	String comment
}
