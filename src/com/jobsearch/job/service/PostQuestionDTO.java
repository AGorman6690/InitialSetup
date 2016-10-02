package com.jobsearch.job.service;

import java.util.List;

public class PostQuestionDTO {
	
	private int id;
	private String text;
	private int formatId;
	private List<String> answerOptions;
	
	
		
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
