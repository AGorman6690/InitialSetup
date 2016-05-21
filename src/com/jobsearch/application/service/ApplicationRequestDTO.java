package com.jobsearch.application.service;

import java.util.List;

import com.jobsearch.model.Answer;

public class ApplicationRequestDTO {
	private int jobId;
	private int userId;
	private List<Answer> answers;
	
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	
}
