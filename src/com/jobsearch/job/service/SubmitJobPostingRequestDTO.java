package com.jobsearch.job.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jobsearch.model.Question;

public class SubmitJobPostingRequestDTO {

	@JsonProperty("jobs")
	List<JobInfoPostRequestDTO> jobs;	

	@JsonProperty("questions")
	List<Question> questions;

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

//	
//	@JsonProperty("jobQuestionPosts")
//	List<JobQuestionPostRequestDTO> jobQuestionPosts;

	public List<JobInfoPostRequestDTO> getJobs() {
		return jobs;
	}

	public void setJobInfoPosts(List<JobInfoPostRequestDTO> jobs) {
		this.jobs = jobs;
	}

//	public List<JobQuestionPostRequestDTO> getJobQuestionPosts() {
//		return jobQuestionPosts;
//	}
//
//	public void setJobQuestionPosts(List<JobQuestionPostRequestDTO> jobQuestionPosts) {
//		this.jobQuestionPosts = jobQuestionPosts;
//	}

	
	
}
