package com.jobsearch.model

import java.util.List

public class Question {

	int questionId
	String text	
	int jobId
	List<AnswerOption> answerOptions
	List<Answer> answers
	
	//0: Yes/N0
	//1: Short answer
	//2: Single answer
	//3: Multi answer
	//Use an enum???????
	int formatId
}
