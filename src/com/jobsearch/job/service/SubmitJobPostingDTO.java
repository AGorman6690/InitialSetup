package com.jobsearch.job.service;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;



public class SubmitJobPostingDTO {

	@JsonProperty("jobs")
	List<PostJobDTO> postJobDtos;	

	@JsonProperty("questions")
	List<PostQuestionDTO> postQuestionDTOs;
	
	public List<PostJobDTO> getPostJobDtos() {
		return postJobDtos;
	}

	public void setPostJobDtos(List<PostJobDTO> postJobDtos) {
		this.postJobDtos = postJobDtos;
	}

	public List<PostQuestionDTO> getPostQuestionDtos() {
		return postQuestionDTOs;
	}

	public void setPostQuestionDtos(List<PostQuestionDTO> postQuestionDtos) {
		this.postQuestionDTOs = postQuestionDtos;
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
