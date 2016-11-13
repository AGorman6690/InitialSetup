package com.jobsearch.job.service

import java.util.List

import org.codehaus.jackson.annotate.JsonProperty

import com.jobsearch.model.Question

class SubmitJobPostingRequestDTO {

	@JsonProperty("jobs")
	List<Job> jobs

	@JsonProperty("questions")
	List<PostQuestionDTO> questions
}
