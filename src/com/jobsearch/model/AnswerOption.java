package com.jobsearch.model;

//***********************************
//***********************************
//This class should be phased out
//***********************************
//***********************************


public class AnswerOption {
	private int answerOptionId;
//	private int questionId;
	private String text;
//	
	public int getAnswerOptionId() {
		return answerOptionId;
	}
	public void setAnswerOptionId(int answerOptionId) {
		this.answerOptionId = answerOptionId;
	}
//	public int getQuestionId() {
//		return questionId;
//	}
//	public void setQuestionId(int questionId) {
//		this.questionId = questionId;
//	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
