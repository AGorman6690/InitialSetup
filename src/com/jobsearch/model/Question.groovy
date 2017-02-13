package com.jobsearch.model

import java.util.List

public class Question {

	int questionId
	String text	
	int jobId
	List<AnswerOption> answerOptions
	List<Answer> answers
	
	int formatId
	
	public static int FORMAT_ID_YES_NO = 0
	public static int FORMAT_ID_SHORT_ANSWER = 1
	public static int FORMAT_ID_SINGLE_ANSWER = 2
	public static int FORMAT_ID_MULTI_ANSWER = 3
}
