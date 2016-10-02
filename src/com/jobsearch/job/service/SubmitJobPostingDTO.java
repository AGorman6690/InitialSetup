package com.jobsearch.job.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jobsearch.model.PostQuestionDto;

public class SubmitJobPostingDTO {

	@JsonProperty("jobs")
	List<PostJobDTO> postJobDtos;	

	@JsonProperty("questions")
	List<PostQuestionDto> postQuestionDtos;
	
	public List<PostJobDTO> getPostJobDtos() {
		return postJobDtos;
	}

	public void setPostJobDtos(List<PostJobDTO> postJobDtos) {
		this.postJobDtos = postJobDtos;
	}

	public List<PostQuestionDto> getPostQuestionDtos() {
		return postQuestionDtos;
	}

	public void setPostQuestionDtos(List<PostQuestionDto> postQuestionDtos) {
		this.postQuestionDtos = postQuestionDtos;
	}


//	
//	@JsonProperty("jobQuestionPosts")
//	List<JobQuestionPostRequestDTO> jobQuestionPosts;


//	public List<JobQuestionPostRequestDTO> getJobQuestionPosts() {
//		return jobQuestionPosts;
//	}
//
//	public void setJobQuestionPosts(List<JobQuestionPostRequestDTO> jobQuestionPosts) {
//		this.jobQuestionPosts = jobQuestionPosts;
//	}

	
	
}
