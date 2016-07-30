package com.jobsearch.model;

import java.util.List;

public class Answer {
//	private int answerOptionId;
//	private List<Integer> answerOptionIds;
	private int questionId;
	private int userId;
	private String text;
//	private List<String> texts;
//	private int answerBoolean;	
//	private List<String> answers;
//	private String answer;
	

	//0: Yes/N0
	//1: Short answer
	//2: Single answer
	//3: Multi answer
//	private int questionFormatId;
	
	
//	public List<String> getTexts() {
//		return texts;
//	}
//	public void setTexts(List<String> texts) {
//		this.texts = texts;
//	}

//	
//	public String getAnswer() {
//		return answer;
//	}
//	public void setAnswer(String answer) {
//		this.answer = answer;
//	}
//	public int getQuestionFormatId() {
//		return questionFormatId;
//	}
//	public void setQuestionFormatId(int questionFormatId) {
//		this.questionFormatId = questionFormatId;
//	}
//	public List<String> getAnswers() {
//		return answers;
//	}
//	public void setAnswers(List<String> answers) {
//		this.answers = answers;
//	}
//	public List<AnswerOption> getAnswerOptions() {
//		return answerOptions;
//	}
//	public void setAnswerOptions(List<AnswerOption> answerOptions) {
//		this.answerOptions = answerOptions;
//	}
//	public int getAnswerBoolean() {
//		return answerBoolean;
//	}
//	public void setAnswerBoolean(int answerBoolean) {
//		this.answerBoolean = answerBoolean;
//	}
//	public List<Integer> getAnswerOptionIds() {
//		return answerOptionIds;
//	}
//	public void setAnswerOptionIds(List<Integer> answerOptionIds) {
//		this.answerOptionIds = answerOptionIds;
//	}
//	public int getAnswerOptionId() {
//		return answerOptionId;
//	}
//	public void setAnswerOptionId(int answerOptionId) {
//		this.answerOptionId = answerOptionId;
//	}
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
