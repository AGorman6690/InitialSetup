package com.jobsearch.job.service;

import java.util.List;

public class PostQuestionDTO {

	//0: Yes/N0
	//1: Short answer
	//2: Single answer
	//3: Multi answer
	private int formatId;


	private int id;
	private String text;
	private List<String> answerOptions;
	private int jobId;


	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public List<String> getAnswerOptions() {
		return answerOptions;
	}
	public void setAnswerOptions(List<String> answerOptions) {
		this.answerOptions = answerOptions;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getFormatId() {
		return formatId;
	}
	public void setFormatId(int formatId) {
		this.formatId = formatId;
	}

}