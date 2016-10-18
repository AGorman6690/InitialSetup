package com.jobsearch.model;

import java.util.List;

public class Question {

	//0: Yes/N0
	//1: Short answer
	//2: Single answer
	//3: Multi answer
	private int formatId;
	private int questionId;
	private String text;	
	private List<AnswerOption> answerOptions;
	private List<Answer> answers;
	

	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public int getFormatId() {
		return formatId;
	}
	public void setFormatId(int formatId) {
		this.formatId = formatId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<AnswerOption> getAnswerOptions() {
		return answerOptions;
	}
	public void setAnswerOptions(List<AnswerOption> answerOptions) {
		this.answerOptions = answerOptions;
	}

}
