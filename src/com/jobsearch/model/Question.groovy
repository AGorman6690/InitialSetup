package com.jobsearch.model

import java.util.List;

class Question {
	//0: Yes/N0
	//1: Short answer
	//2: Single answer
	//3: Multi answer
	//Use an enum???????
	int questionId
	String text
	int formatId
	int jobId
	List<AnswerOption> answerOptions
	List<Answer> answers
}
