package com.jobsearch.model;

import java.util.List;

public class Answer {
	private int answerOptionId;
	private List<Integer> answerOptionIds;
	private int questionId;
	private int userId;
	private String answerText;
	private int answerBoolean;	
	private List<String> answers;
	private int questionFormatId;
	
	
	public int getQuestionFormatId() {
		return questionFormatId;
	}
	public void setQuestionFormatId(int questionFormatId) {
		this.questionFormatId = questionFormatId;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
//	public List<AnswerOption> getAnswerOptions() {
//		return answerOptions;
//	}
//	public void setAnswerOptions(List<AnswerOption> answerOptions) {
//		this.answerOptions = answerOptions;
//	}
	public int getAnswerBoolean() {
		return answerBoolean;
	}
	public void setAnswerBoolean(int answerBoolean) {
		this.answerBoolean = answerBoolean;
	}
	public List<Integer> getAnswerOptionIds() {
		return answerOptionIds;
	}
	public void setAnswerOptionIds(List<Integer> answerOptionIds) {
		this.answerOptionIds = answerOptionIds;
	}
	public int getAnswerOptionId() {
		return answerOptionId;
	}
	public void setAnswerOptionId(int answerOptionId) {
		this.answerOptionId = answerOptionId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	
}
